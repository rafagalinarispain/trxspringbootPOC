package com.example.demo.service;

import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoClient;
import com.mongodb.MongoCommandException;
import com.mongodb.MongoException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

import org.springframework.lang.NonNull;

@Service
public class UpdateService {

    private static final Logger logger = LoggerFactory.getLogger(UpdateService.class);

    private final MongoTemplate mongoTemplate;
    private final MongoClient mongoClient;
    private final PlatformTransactionManager transactionManager;
    private final TransactionTemplate transactionTemplate;

    public UpdateService(MongoTemplate mongoTemplate, MongoClient mongoClient, PlatformTransactionManager transactionManager) {
        this.mongoTemplate = mongoTemplate;
        this.mongoClient = mongoClient;
        this.transactionManager = transactionManager;
        this.transactionTemplate = new TransactionTemplate(transactionManager);

    }

    @Transactional(value = "mongoTransactionManager", propagation = Propagation.REQUIRED)
    @Retryable(value = {MongoCommandException.class, MongoException.class}, backoff = @Backoff(delay = 1000), maxAttempts = 4)
    public void updateColl1(int filterValue, int updateValue) {
        try {
            Query query = new Query(Criteria.where("a").is(filterValue));
            Update update = new Update().inc("b", updateValue);
            mongoTemplate.updateFirst(query, update, "coll1");
            logger.info("Successfully updated coll1 with a: {} To the Value b: {}", filterValue, updateValue);
        } catch (Exception e) {
            logger.error("Error updating coll1 with filterValue a: {}, updateValue b: {}", filterValue, updateValue, e);
            throw e;
        }
    }

    @Transactional(value = "mongoTransactionManager", propagation = Propagation.REQUIRED)
    @Retryable(value = {MongoCommandException.class, MongoException.class}, backoff = @Backoff(delay = 1000), maxAttempts = 4)
    public void updateColl2(int filterValue, int updateValue) {
        try {
            Query query = new Query(Criteria.where("a").is(filterValue));
            Update update = new Update().set("b", updateValue);
            mongoTemplate.updateFirst(query, update, "coll2");
            logger.info("Successfully updated coll2 with a: {} To the Value b: {}", filterValue, updateValue);
        } catch (Exception e) {
            logger.error("Error updating coll2 with filterValue: {}, updateValue: {}", filterValue, updateValue, e);
            throw e;
        }
    }

    public void updateColl3(int filterValue, int updateValue) {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("updateColl3Transaction");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

        TransactionStatus status = transactionManager.getTransaction(def);
        try {
            Query query = new Query(Criteria.where("a").is(filterValue));
            Update update = new Update().set("b", updateValue);
            mongoTemplate.updateFirst(query, update, "coll3");
            transactionManager.commit(status);
            logger.info("Successfully updated coll3 with filterValue: {}, updateValue: {}", filterValue, updateValue);
        } catch (Exception e) {
            transactionManager.rollback(status);
            logger.error("Error updating coll3 with filterValue: {}, updateValue: {}", filterValue, updateValue, e);
            throw e;
        }
    }

    public void updateColl4(int filterValue, int updateValue) {
        ClientSession session = mongoClient.startSession();
        try {
            session.startTransaction();
            Query query = new Query(Criteria.where("a").is(filterValue));
            Update update = new Update().set("b", updateValue);
            mongoTemplate.withSession(() -> session).execute(action -> {
                mongoTemplate.updateFirst(query, update, "coll4");
                return null;
            });
            session.commitTransaction();
            logger.info("Successfully updated coll4 with a: {} To the Value b: {}", filterValue, updateValue);
        } catch (Exception e) {
            session.abortTransaction();
            logger.error("Error updating coll4 with b: {}", filterValue, e);
            throw e;
        } finally {
            session.close();
        }
    }

    public void updateColl5(int filterValue, int updateValue) {
        try {
            transactionTemplate.execute(new TransactionCallbackWithoutResult() {
                @Override
                protected void doInTransactionWithoutResult(@NonNull TransactionStatus status) {
                    Query query = new Query(Criteria.where("a").is(filterValue));
                    Update update = new Update().set("b", updateValue);
                    mongoTemplate.updateFirst(query, update, "coll5");
                }
            });
            logger.info("Successfully updated coll5 with filterValue: {}, updateValue: {}", filterValue, updateValue);
        } catch (Exception e) {
            logger.error("Error updating coll5 with filterValue: {}, updateValue: {}", filterValue, updateValue, e);
            throw e;
        }
    }
    
}
