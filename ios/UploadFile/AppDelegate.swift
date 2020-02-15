//
//  AppDelegate.swift
//  UploadFile
//
//  Created by Fandy Gotama on 09/02/20.
//  Copyright © 2020 Adrena Teknologi Indonesia. All rights reserved.
//

import UIKit

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {
    var window: UIWindow?
    
    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        
        let controller = ViewController()
        
        controller.title = "Hello World"
        
        let navigationController = UINavigationController(rootViewController: controller)
        
        window = UIWindow(frame: UIScreen.main.bounds)
        window?.backgroundColor = .white
        window?.rootViewController = navigationController
        window?.makeKeyAndVisible()
        
        return true
    }
}

