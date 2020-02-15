//
//  ViewController.swift
//  UploadFile
//
//  Created by Fandy Gotama on 09/02/20.
//  Copyright © 2020 Adrena Teknologi Indonesia. All rights reserved.
//

import UIKit
import Common

class ViewController: UIViewController {
    let loading: UIView = {
        let v = UIView()
        
        let loading = UILabel()
        
        loading.text = "Loading..."
        loading.translatesAutoresizingMaskIntoConstraints = false
        
        v.translatesAutoresizingMaskIntoConstraints = false
        v.addSubview(loading)
        
        NSLayoutConstraint.activate([
            loading.centerXAnchor.constraint(equalTo: v.centerXAnchor),
            loading.centerYAnchor.constraint(equalTo: v.centerYAnchor)
        ])
        
        return v
    }()
    
    let lblConfidence: UILabel = {
        let v = UILabel()
        
        v.translatesAutoresizingMaskIntoConstraints = false
        
        return v
    }()
    
    let lblResult: UILabel = {
        let v = UILabel()
        
        v.numberOfLines = 0
        v.translatesAutoresizingMaskIntoConstraints = false
        
        return v
    }()
    
    private lazy var _viewModel: ViewModel = {
        let mapper = OCRMapper()
        
        let service = OCRCloudService<OCR>(host: "https://ocr-fdvxbwj6ua-an.a.run.app", mapper: mapper)
        let repository = RepositoryImpl<NSString, OCR>(service: service, cacheService: nil)
        let useCase = UseCaseImpl<NSString, OCR>(repository: repository)
        
        return ViewModelImpl<NSString, OCR>(useCase: useCase)
    }()
    
    private lazy var _binding: ViewModelBinding = {
        ViewModelBinding()
    }()
    
    deinit {
        _binding.dispose()
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
 
        view.addSubview(lblConfidence)
        view.addSubview(lblResult)
        view.addSubview(loading)
        
        NSLayoutConstraint.activate([
            loading.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            loading.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            loading.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor),
            loading.bottomAnchor.constraint(equalTo: view.safeAreaLayoutGuide.bottomAnchor),
            
            lblConfidence.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 20),
            lblConfidence.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -20),
            lblConfidence.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor, constant: 20),
            
            lblResult.leadingAnchor.constraint(equalTo: lblConfidence.leadingAnchor),
            lblResult.trailingAnchor.constraint(equalTo: lblConfidence.trailingAnchor),
            lblResult.topAnchor.constraint(equalTo: lblConfidence.bottomAnchor, constant: 10),
        ])
        
        binding()
        
        let controller = UIImagePickerController()
        
        controller.modalPresentationStyle = .fullScreen
        controller.allowsEditing = true
        controller.delegate = self
        controller.sourceType = .photoLibrary
        
        present(controller, animated: true, completion: nil)
    }
    
    private func saveFile(path: String = "cache/", image: UIImage, completion: ((URL?) -> Void)?) {
        
        DispatchQueue.global(qos: .background).async { [weak self] in
            if let data = image.jpegData(compressionQuality: 1) {
                do {
                    self?.createDirectory()
                    
                    let documentDirectory = try FileManager.default.url(for: .documentDirectory, in: .userDomainMask, appropriateFor: nil, create: false)
                    
                    let url = documentDirectory.appendingPathComponent("\(path)\(UUID().uuidString).jpg")
                    
                    try data.write(to: url)
                    
                    DispatchQueue.main.async {
                        completion?(url)
                    }
                } catch {
                    completion?(nil)
                }
            }
        }
    }
    
    private func createDirectory(path: String = "cache/") {
        
        do {
            let documentsDirectory = try FileManager.default.url(for: .documentDirectory, in: .userDomainMask, appropriateFor:nil, create:false)
            
            let dataPath = documentsDirectory.appendingPathComponent(path)
            
            try FileManager.default.createDirectory(atPath: dataPath.path, withIntermediateDirectories: true, attributes: nil)
        } catch {
            assertionFailure()
        }
    }
    
    private func binding() {
        _binding.subscribe(observable: _viewModel.outputs.loading) { [weak self] result in
            guard let strongSelf = self, let loading = result as? Bool else { return }
            
            strongSelf.loading.isHidden = !loading
        }
        
        _binding.subscribe(observable: _viewModel.outputs.result) { [weak self] result in
            guard let strongSelf = self, let ocr = result as? OCR else { return }
            
            strongSelf.lblConfidence.text = "Confidence: \(ocr.confidence)"
            strongSelf.lblResult.text = ocr.result
        }
    }
}

extension ViewController: UIImagePickerControllerDelegate, UINavigationControllerDelegate {
    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey : Any]) {
        let savedImage: UIImage?
        
        if let image = info[.editedImage] as? UIImage {
            savedImage = image
        } else if let image = info[.originalImage] as? UIImage {
            savedImage = image
        } else {
            savedImage = nil
        }
        
        if let image = savedImage {
            saveFile(image: image, completion: { [weak self] in
                if let url = $0 {
                    self?._viewModel.inputs.execute(request_: url)
                }
            })
        }
        
        dismiss(animated: true, completion: nil)
    }
    
    func imagePickerControllerDidCancel(_ picker: UIImagePickerController) {
        dismiss(animated: true, completion: nil)
    }
}

