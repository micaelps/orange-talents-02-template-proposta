package br.com.zup.proposta.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Component;

@Component
public class EncryptDecrypt {

    private static String SECRET_STATIC;
    private static String SALT_STATIC;

    @Value("${encrypt.decrypt.secret}")
    private void setSecretStatic(String secret){
        EncryptDecrypt.SECRET_STATIC = secret;
    }

    @Value("${encrypt.decrypt.salt}")
    private void setSaltStatic(String salt){
        EncryptDecrypt.SALT_STATIC = salt;
    }

    private static TextEncryptor get(){
        return Encryptors.text(SECRET_STATIC, SALT_STATIC);
    }

    public static String encrypt(String data){
        return get().encrypt(data);
    }

    public static String decrypt(String data){
        return get().decrypt(data);
    }
}
