package com.cricket46.games.cricketcards.services;

import com.cricket46.games.cricketcards.domain.CricketCardUserAggregate;
import com.cricket46.games.cricketcards.domain.QCricketCardUserAggregate;
import com.cricket46.games.cricketcards.model.User;
import com.cricket46.games.cricketcards.repository.CricketCardUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

            user.setUserId(userDbOpt.get().getUserId());
            user.setUserName(userDbOpt.get().getUserName());
            user.setMobile(userDbOpt.get().getMobile());
        }
        return user;

    }

    public User getUser(long userId){
        Optional<CricketCardUserAggregate> userOpt = cricketCardUserRepository.findOne(qCricketCardUserAggregate.userId.eq(userId));
        User user = new User();
        if(userOpt.isPresent()){
            user.setUserId(userOpt.get().getUserId());
            user.setUserName(userOpt.get().getUserName());
            user.setMobile(userOpt.get().getMobile());
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

    public List<User> getUsersList() {
        List<CricketCardUserAggregate> cricketCardUserAggregates = cricketCardUserRepository.findAll();
        List<User> users = new ArrayList<>();
        if(null != cricketCardUserAggregates) {
            users = cricketCardUserAggregates.stream().map(cricketCardUserAggregate -> {
                User user = new User();
                user.setUserId(cricketCardUserAggregate.getUserId());
                user.setUserName(cricketCardUserAggregate.getUserName());
                user.setMobile(cricketCardUserAggregate.getMobile());
                return user;
            }).collect(Collectors.toList());
        }
        return users;
    }

    public List<User> getFriendList(long playerId) {
        return getUsersList().stream().filter(user -> user.getUserId() != playerId).collect(Collectors.toList());
    }
}
