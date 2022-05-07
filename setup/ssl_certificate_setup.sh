keytool -genkeypair -alias springboot -keyalg RSA -keysize 4096 -storetype PKCS12 -keystore robocupms.p12 -validity 3650 -storepass f4R03eRRG3

keytool -export -keystore robocupms.p12 -alias springboot -file robocupms_Certificate.crt