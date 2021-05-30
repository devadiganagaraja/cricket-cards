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

    public boolean authenticateUser(String mobile, String password) {
        Optional<CricketCardUserAggregate> userDbOpt = cricketCardUserRepository.findOne(qCricketCardUserAggregate.mobile.eq(mobile).and(qCricketCardUserAggregate.password.eq(password)));
        return  userDbOpt.isPresent();
    }

    public User addUser(User user) {
        cricketCardUserRepository.save(populateDBUser(user));
        return user;
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
