/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.gestionscolaire.initDatas;

import com.example.gestionscolaire.Users.entity.EStatusUser;
import com.example.gestionscolaire.Users.entity.StatusUser;
import com.example.gestionscolaire.Users.repository.IStatusUserRepo;
import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 *
 * @author Casimir
 */

@Component
@AllArgsConstructor
@Order(3)
public class InitUserStatus implements ApplicationRunner{

    private IStatusUserRepo iTypeAccountRepository;
    
    @Override
    public void run(ApplicationArguments args) throws Exception {;
        System.out.println("initialisation des status utilisateurs");
        StatusUser userEnable = new StatusUser(EStatusUser.USER_ENABLED);
        StatusUser userDisable = new StatusUser(EStatusUser.USER_DISABLED);

        if (!iTypeAccountRepository.existsByName(EStatusUser.USER_ENABLED)) {
            iTypeAccountRepository.save(userEnable);
        }
        if (!iTypeAccountRepository.existsByName(EStatusUser.USER_DISABLED)) {
            iTypeAccountRepository.save(userDisable);
        }
               
    }
    
}
