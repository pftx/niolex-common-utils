/**
 * JWTDemo.java
 *
 * Copyright 2017 the original author or authors.
 *
 * We licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package org.apache.niolex.common.jwt;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.niolex.commons.bean.BeanUtil;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator.Builder;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

/**
 * JSON Web Token (JWT) is an open standard (RFC 7519) that defines a compact and self-contained way for securely
 * transmitting information between parties as a JSON object.
 * 
 * JSON Web Tokens consist of three parts separated by dots (.), which are:
 * 
 * <pre>
 * .Header
 * .Payload
 * .Signature
 * 
 * Therefore, a JWT typically looks like the following.
 * xxxxx.yyyyy.zzzzz
 * </pre>
 * 
 * @author <a href="mailto:xiejiyun@foxmail.com">Xie, Jiyun</a>
 * @version 3.0.1
 * @since Jan 23, 2017
 */
public class JWTDemo {

    /**
     * @param args
     */
    public static void main(String[] args) {
        signAndVerify();

    }

    public static void signAndVerify() {
        try {
            // Sign JWT.
            Builder bui = JWT.create();
            Map<String, Object> headerClaims = new HashMap<>();

            headerClaims.put("who", "Lily");
            headerClaims.put("when", "2017-01-01");
            headerClaims.put("what", "deposit");

            bui.withHeader(headerClaims);

            bui.withClaim("id", 65536).withClaim("name", "Lily").withClaim("how", "Rampup");
            String token2 = bui.withIssuer("Lex").withIssuedAt(new Date()).sign(Algorithm.HMAC256("AbIJTDMFc"));
            System.out.println(token2);

            // Verify JWT.
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256("AbIJTDMFc")).withIssuer("Lex").build();
            DecodedJWT jwt2 = verifier.verify(token2);
            System.out.println(BeanUtil.toString(jwt2));
        } catch (Exception exception) {
            // Invalid token
            exception.printStackTrace();
        }
    }

}
