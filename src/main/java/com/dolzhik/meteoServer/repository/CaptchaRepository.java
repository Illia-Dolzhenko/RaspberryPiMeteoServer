package com.dolzhik.meteoServer.repository;

import com.dolzhik.meteoServer.entity.Captcha;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CaptchaRepository extends CrudRepository<Captcha, Long> {
    @Modifying
    @Transactional
    @Query(value = "delete from captcha where CURRENT_TIMESTAMP() > DATEADD(MINUTE, 5, created)",
            nativeQuery = true)
    void removeOldCaptchas();

    Captcha getCaptchaById(long id);
}
