package com.zb.cms.order.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zb.cms.order.domain.redis.Cart;
import com.zb.cms.order.exception.CustomException;
import com.zb.cms.order.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
@Slf4j
@RequiredArgsConstructor
public class RedisClient {

    private final RedisTemplate<String ,Object > redisTemplate;
    private static final ObjectMapper mapper = new ObjectMapper();

    public <T> T get(Long key,Class<T> classType){
        return get(key.toString(),classType);
    }

    private <T> T get(String key, Class<T> classType) {
        String redisValue = (String) redisTemplate.opsForValue().get(key);
        if (ObjectUtils.isEmpty(redisValue)) {
            return null;
        } else {
            try {
                return mapper.readValue(redisValue, classType);
            } catch (JsonProcessingException e) {
                log.error("Parsing error", e);
                return null;
            }
        }
    }
    public void put(Long key, Cart cart){
        put(key.toString(),cart);
    }

    public void put(String key,Cart cart){
        try{
            redisTemplate.opsForValue().set(key,mapper.writeValueAsString(cart));
        }catch (JsonProcessingException e){
            throw new CustomException(ErrorCode.CART_CHANGE_FAIL);
        }
    }
}
