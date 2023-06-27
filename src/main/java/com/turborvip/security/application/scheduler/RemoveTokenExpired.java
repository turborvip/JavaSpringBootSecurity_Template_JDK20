package com.turborvip.security.application.scheduler;

import com.turborvip.security.application.repositories.TokenRepository;
import com.turborvip.security.application.services.TokenService;
import com.turborvip.security.domain.entity.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RemoveTokenExpired {
    @Autowired
    TokenService tokenService;
    @Autowired
    private TokenRepository tokenRepository;

    // TODO remove token expired every new hour = 0
    //  To check
    //  @Scheduled(fixedRate = 1800000)
    @Scheduled(cron = "0 0 */2 * * ?")
    public void removeTokenExpired() {
        System.out.println("Run cronjob remove token expired!");
        List<Token> listTokenExpired = tokenService.findListTokenExpired();
        listTokenExpired.forEach(tokenRepository::delete);
    }

}
