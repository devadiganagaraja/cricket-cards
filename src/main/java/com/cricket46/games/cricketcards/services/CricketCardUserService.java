package com.cricket46.games.cricketcards.services;

import com.cricket46.games.cricketcards.domain.CricketCardUserAggregate;
import com.cricket46.games.cricketcards.domain.QCricketCardUserAggregate;
import com.cricket46.games.cricketcards.model.User;
import com.cricket46.games.cricketcards.repository.CricketCardUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CricketCardUserService {
    @Autowired
    CricketCardUserRepository cricketCardUserRepository;


    @Autowired
    QCricketCardUserAggregate qCricketCardUserAggregate;

    public User authenticateUser(String mobile, String password) {
        Optional<CricketCardUserAggregate> userDbOpt = cricketCardUserRepository.findOne(qCricketCardUserAggregate.mobile.eq(mobile).and(qCricketCardUserAggregate.password.eq(password)));
        User user = new User();
        if(userDbOpt.isPresent()){

            user.setUserName(userDbOpt.get().getUserName());
            user.setMobile(userDbOpt.get().getMobile());
        }
        return user;

    }

    public Boolean addUser(User user) {
        try {
            cricketCardUserRepository.save(populateDBUser(user));
            return true;
        }catch (Exception e){
            return false;
        }
    }

    private CricketCardUserAggregate populateDBUser(User user) {
        CricketCardUserAggregate cricketCardUserAggregate = new CricketCardUserAggregate();
        cricketCardUserAggregate.setUserId(user.getMobile().hashCode());
        cricketCardUserAggregate.setUserName(user.getUserName());
        cricketCardUserAggregate.setMobile(user.getMobile());
        cricketCardUserAggregate.setPassword(user.getPassword());
        return cricketCardUserAggregate;
    }
}
