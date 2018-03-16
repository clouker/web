package org.clc.mongo.dao;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class MongoBaseDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    public MongoCollection<Document> getCollection(int limit,Bson sort,int skip){
        MongoCollection<Document> mongoCollection = mongoTemplate.getMongoDbFactory().getDb("feature_peoplexs_test").getCollection("PERSON_TOTAL");
        return mongoCollection;
    };
}
