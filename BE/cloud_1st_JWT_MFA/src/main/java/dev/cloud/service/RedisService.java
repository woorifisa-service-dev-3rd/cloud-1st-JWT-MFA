package dev.cloud.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

/**
 * RedisService는 Redis를 사용하여 데이터 저장, 조회, 삭제, 만료 기능을 제공하는 서비스 클래스입니다.
 * StringRedisTemplate을 사용하여 Redis에서 문자열 데이터를 처리합니다.
 */
@Service
@RequiredArgsConstructor
public class RedisService {

    // Redis와의 문자열 데이터를 처리하는 StringRedisTemplate 인스턴스
    private final StringRedisTemplate redisTemplate;

    /**
     * 주어진 키에 해당하는 데이터를 Redis에서 조회합니다.
     *
     * @param key Redis에 저장된 데이터의 키
     * @return 키에 해당하는 값이 존재할 경우 해당 값, 없으면 null 반환
     */
    public String getData(String key) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        return valueOperations.get(key); // 키에 해당하는 값을 가져옵니다.
    }

    /**
     * 주어진 키의 데이터가 Redis에 존재하는지 확인합니다.
     *
     * @param key 존재 여부를 확인할 키
     * @return 데이터가 존재하면 true, 없으면 false 반환
     */
    public boolean existData(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key)); // 키가 존재하는지 확인합니다.
    }

    /**
     * 주어진 키와 값을 Redis에 저장하며, 특정 만료 시간을 설정합니다.
     *
     * @param key      저장할 데이터의 키
     * @param value    저장할 데이터의 값
     * @param duration 데이터의 만료 시간(초 단위)
     */
    public void setDataExpire(String key, String value, long duration) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        Duration expireDuration = Duration.ofSeconds(duration); // 만료 시간을 설정합니다.
        valueOperations.set(key, value, expireDuration); // 키, 값과 함께 만료 시간을 지정하여 데이터를 저장합니다.
    }

    /**
     * 주어진 키에 해당하는 데이터를 Redis에서 삭제합니다.
     *
     * @param key 삭제할 데이터의 키
     */
    public void deleteData(String key) {
        redisTemplate.delete(key); // 해당 키의 데이터를 삭제합니다.
    }

}
