package com.insomnia.kayakstrava.services;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.insomnia.kayakstrava.models.StravaActivity;

public interface StravaActivityRepo extends MongoRepository<StravaActivity, String> {

}
