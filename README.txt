The only difference on client side between using symmetric and asymmetric keys is in the way token is parsed.
Using symmetric keys:
 - retrieve token and parse it with:
      return Jwts.parser()
                     .setSigningKey(secret) // Use symmetric key retrieved with @Value to validate token
                     .parseClaimsJws(token)
                     .getBody();

Using asymmetric keys:
 - Retrieve certificate
 - retrieve token and parse it with:
      return Jwts.parser()
                     .setSigningKey(getPublicKey()) // Use public key to validate token
                     .parseClaimsJws(token)
                     .getBody();

private PublicKey getPublicKey() {
        try {
            ClassLoader classLoader = JwtTokenService.class.getClassLoader();

            final URL resource = classLoader.getResource(CERTIFICATE_NAME);
            if (resource != null) {
                File file = new File(resource.getFile());
                KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
                keystore.load(new FileInputStream(file), secret.toCharArray());
                Certificate cert = keystore.getCertificate(CERTIFICATE_ALIAS);
                return cert.getPublicKey();
            } else {
                throw new OAuth2Exception(KEYSTORE_FILE_NOT_AVAILABLE);
            }
        } catch (Exception e) {
            throw new OAuth2Exception(KEYSTORE_FILE_NOT_AVAILABLE);
        }
    }

NOTE:
 - Call POST to retrieve the token: http://localhost:8889/auth_sym_key/token
 - Call http://localhost:8080/users  with type of authorisation: OAuth2 and access token from above call