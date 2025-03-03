package expo.modules.updates.loader

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import java.security.cert.CertificateException
import java.security.cert.CertificateExpiredException

private const val testBody = "{\"id\":\"0754dad0-d200-d634-113c-ef1f26106028\",\"createdAt\":\"2021-11-23T00:57:14.437Z\",\"runtimeVersion\":\"1\",\"assets\":[{\"hash\":\"cb65fafb5ed456fc3ed8a726cf4087d37b875184eba96f33f6d99104e6e2266d\",\"key\":\"489ea2f19fa850b65653ab445637a181.jpg\",\"contentType\":\"image/jpeg\",\"url\":\"http://192.168.64.1:3000/api/assets?asset=updates/1/assets/489ea2f19fa850b65653ab445637a181&runtimeVersion=1&platform=android\",\"fileExtension\":\".jpg\"}],\"launchAsset\":{\"hash\":\"323ddd1968ee76d4ddbb16b04fb2c3f1b6d1ab9b637d819699fecd6fa0ffb1a8\",\"key\":\"696a70cf7035664c20ea86f67dae822b.bundle\",\"contentType\":\"application/javascript\",\"url\":\"http://192.168.64.1:3000/api/assets?asset=updates/1/bundles/android-696a70cf7035664c20ea86f67dae822b.js&runtimeVersion=1&platform=android\",\"fileExtension\":\".bundle\"}}"
private const val testCertificate = "-----BEGIN CERTIFICATE-----\nMIIDfzCCAmegAwIBAgIJLGiqnjmA9JmpMA0GCSqGSIb3DQEBCwUAMGkxFDASBgNV\nBAMTC2V4YW1wbGUub3JnMQswCQYDVQQGEwJVUzERMA8GA1UECBMIVmlyZ2luaWEx\nEzARBgNVBAcTCkJsYWNrc2J1cmcxDTALBgNVBAoTBFRlc3QxDTALBgNVBAsTBFRl\nc3QwHhcNMjExMTIyMTc0NzQzWhcNMjIxMTIyMTc0NzQzWjBpMRQwEgYDVQQDEwtl\neGFtcGxlLm9yZzELMAkGA1UEBhMCVVMxETAPBgNVBAgTCFZpcmdpbmlhMRMwEQYD\nVQQHEwpCbGFja3NidXJnMQ0wCwYDVQQKEwRUZXN0MQ0wCwYDVQQLEwRUZXN0MIIB\nIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAucg/fRwgYLxO4fDG1W/ew4Wu\nkqp2+j9mLyA18sd8noCT0eSJwxMTLJJq4biNx6kJEVSQdodN3e/qSJndz+ZHA7b1\n6Do3Ecg5oRvl3HEwaH4AkM2Lj87VjgfxPUsiSHtPd+RTbxnOy9lGupQa/j71WrAq\nzJpNmhP70vzkY4EVejn52kzRPZB3kTxkjggFrG/f18Bcf4VYxN3aLML32jih+UC0\n6fv57HNZZ3ewGSJrLcUdEgctBWiz1gzwF6YdXtEJ14eQbgHgsLsXaEQeg2ncGGxF\n/3rIhsnlWjeIIya7TS0nvqZHNKznZV9EWpZQBFVoLGGrvOdU3pTmP39qbmY0nwID\nAQABoyowKDAOBgNVHQ8BAf8EBAMCB4AwFgYDVR0lAQH/BAwwCgYIKwYBBQUHAwMw\nDQYJKoZIhvcNAQELBQADggEBAALcH9Jb3wq64YkNxUIa25T9umhr4uRe94ESHujM\nIRrBbbqu1p3Vs8N3whZNhcL6Djb4ob18m/aGKbF+UQBMvhn23qRCG6KKzIeDY6Os\n8tYyIwush2XeOFA7S5syPqVBI6PrRBDMCLmAJO4qTM2p0f+zyFXFuytCXOv2fA3M\n88aYVmU7NIfBTFdqNIgSt1yj7FKvd5zgoUyu7mTVdzY59xQzkzYTsnobY2XrTcvY\n6wyRqOAQ86wR8OvDjHB5y/YN2Pdg7d9jUFBCX6Ohr7W3GHrjAadKwq+kbH1aP0oB\nQTFLQQfl3gtJ3Dl/5iBQD38sCIkA54FPSsKTRw3mC4DImBQ=\n-----END CERTIFICATE-----"
private const val testSignature = "sig=\"VpuLfRlB0DizR+hRWmedPGHdx/nzNJ8OomMZNGHwqx64zrx1XezriBoItup/icOlXFrqs6FHaul4g5m41JfEWCUbhXC4x+iNk//bxozEYJHmjbcAtC6xhWbMMYQQaUjuYk7rEL987AbOWyUI2lMhrhK7LNzBaT8RGqBcpEyAqIOMuEKcK0faySnWJylc7IzxHmO8jlx5ufzio8301wej8mNW5dZd7PFOX8Dz015tIpF00VGi29ShDNFbpnalch7f92NFs08Z0g9LXndmrGjNL84Wqd4kq5awRGQObrCuDHU4uFdZjtr4ew0JaNlVuyUrrjyDloBdq0aR804vuDXacQ==\""
private const val testCertificateValidityExpired = "-----BEGIN CERTIFICATE-----\nMIIDfzCCAmegAwIBAgIJUMTYibP/SwICMA0GCSqGSIb3DQEBCwUAMGkxFDASBgNV\nBAMTC2V4YW1wbGUub3JnMQswCQYDVQQGEwJVUzERMA8GA1UECBMIVmlyZ2luaWEx\nEzARBgNVBAcTCkJsYWNrc2J1cmcxDTALBgNVBAoTBFRlc3QxDTALBgNVBAsTBFRl\nc3QwHhcNMjIwMTEzMjEyNDA2WhcNMjIwMTEzMjEyNDA2WjBpMRQwEgYDVQQDEwtl\neGFtcGxlLm9yZzELMAkGA1UEBhMCVVMxETAPBgNVBAgTCFZpcmdpbmlhMRMwEQYD\nVQQHEwpCbGFja3NidXJnMQ0wCwYDVQQKEwRUZXN0MQ0wCwYDVQQLEwRUZXN0MIIB\nIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAuFzjX8khaNUKLBAnywPK7olI\nkFIYrCHCRMJrsd8h/bES9ieFuhycNw8/4JdTd3fq7j8WFYR9OAUeddmh7Z9dB5DR\nQOg1Iy4raODPPpgtTsjQSLf/bDh47WBmqnCapWTImphp9ABfqrWRT404DrV4rlKP\ntp4a0PF2HDeGDKu9GrCt6Ui+lA95rQzkBosE603ljkT8jJfE2McboGi7RXvyUl8N\nXpx9Pzvdkf5yUChgtWF8LZIHIKU1Ozcx5GfIGIQ44JRZIOpHS4jRPkItrOqDvvuZ\nFRLdrmEDr+QIeHutTKeTOPyhiRxz33L6S+DT7hGvQxtcuFAfri866CoGp3GxawID\nAQABoyowKDAOBgNVHQ8BAf8EBAMCB4AwFgYDVR0lAQH/BAwwCgYIKwYBBQUHAwMw\nDQYJKoZIhvcNAQELBQADggEBAA2U+1FgCEA96aNeb33Ce37tUWRcVH8sSQdaFRJa\nvEU8jW+d+bXWS9DFfarLdBP5BvOCd1lr63vqkUbB04MgsGtLARmR1fRxBs18XKIl\njiOeDNvsRwkG7zz5Td6/J0Zt6m0Csl5RwSIaN7wM1QAkD9NukMBTQsAghMSfyqry\na1b0zVegz5nR744dcs82QxAnxD6UFQgu2pvV0TklAdbLI1kgnv7v+7V4g+UMOMIa\nEjtx2ikPNziuPxxS6zPkWiFOgmii+jtWkJt55YZRQsrzmKHX8iQL79FguqHchfeM\njfcCAqPuMRzQErPGvS9rmdofsvuXK/3xldt/ujFyLw6TGZ8=\n-----END CERTIFICATE-----"
private const val testCertificateNoKeyUsage = "-----BEGIN CERTIFICATE-----\nMIIDUzCCAjugAwIBAgIJSvGkSo8+4UukMA0GCSqGSIb3DQEBCwUAMGkxFDASBgNV\nBAMTC2V4YW1wbGUub3JnMQswCQYDVQQGEwJVUzERMA8GA1UECBMIVmlyZ2luaWEx\nEzARBgNVBAcTCkJsYWNrc2J1cmcxDTALBgNVBAoTBFRlc3QxDTALBgNVBAsTBFRl\nc3QwHhcNMjIwMTEzMjEyNTAxWhcNMjMwMTEzMjEyNTAxWjBpMRQwEgYDVQQDEwtl\neGFtcGxlLm9yZzELMAkGA1UEBhMCVVMxETAPBgNVBAgTCFZpcmdpbmlhMRMwEQYD\nVQQHEwpCbGFja3NidXJnMQ0wCwYDVQQKEwRUZXN0MQ0wCwYDVQQLEwRUZXN0MIIB\nIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEArp9E7jQyOsA2IyGCX5tR9hsR\n/FCUBkSfpqlQejtKMlaX3XUmhDi8RgT0SGsqEztpmXVWHQuXHMJ4//N9kdSkZtK0\n5D2ZxEIrdOq/R+KfVtlLbwiOc1c4ApVIrJofXNntKK7nUR2AwF2pLjqD6wV6Pzsi\nYOtvykwZE4pvUnEluN6K5Sj509gq+vh68nmY2F/i16zZ/jULoRYmQHVhb6Dl2SEW\nuMIJB0hXPa0hC/hV1bmWDeCGOHgv4Ah0lK7KySlah29Z9a6Xxs6doYjGKc4HEOSb\nkEfSkIysqIn69FfzKlkSGj6oKtZezUfXHpgQcBjuTroePHy19QBM1CfSTJ10wQID\nAQABMA0GCSqGSIb3DQEBCwUAA4IBAQCAE35etL3iKVYTMA1nlmPgp9JBJzu33xT9\npY9pIG2Xuco4yXgFKrDC3FBp4/jp2eKcABzAUvlnU5g7WW3HoYwvXuceGR5bt71r\n36fkFU0V1wWg8cro0uJI8obBX2RceZ4KJze7+TPygRAXbZHl+I0MJs0NHkoRdBMb\n5v5N2i1BkCzP+yxkAogQTQ8or0761QMUDxbtcadUJqworNzJCK0Fk4wmX41JLPNh\nv0DjycUkJKikXHDiyE41Mmn7feIPepNOGEM6tmrLFut3iXrQPVar9ZtJyTnVdGSR\nfLIHcXU2vKNav2X36xFGkqAVMO3Y2ut7mfwpd3TcSrsjng54MjZo\n-----END CERTIFICATE-----"
private const val testCertificateNoCodeSigningExtendedKeyUsage = "-----BEGIN CERTIFICATE-----\nMIIDdTCCAl2gAwIBAgIJCJvpeGIP+bCfMA0GCSqGSIb3DQEBCwUAMGkxFDASBgNV\nBAMTC2V4YW1wbGUub3JnMQswCQYDVQQGEwJVUzERMA8GA1UECBMIVmlyZ2luaWEx\nEzARBgNVBAcTCkJsYWNrc2J1cmcxDTALBgNVBAoTBFRlc3QxDTALBgNVBAsTBFRl\nc3QwHhcNMjIwMTEzMjEyOTMwWhcNMjMwMTEzMjEyOTMwWjBpMRQwEgYDVQQDEwtl\neGFtcGxlLm9yZzELMAkGA1UEBhMCVVMxETAPBgNVBAgTCFZpcmdpbmlhMRMwEQYD\nVQQHEwpCbGFja3NidXJnMQ0wCwYDVQQKEwRUZXN0MQ0wCwYDVQQLEwRUZXN0MIIB\nIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA82yZpjIloSl5wQeZl1Sc9RP0\nJ7CYJvG1dVhZ253jeDaItq7iyM4Aplz4wPxGalOlKeg8ozT4/Iz0xA2f1wuOxvXW\nICY4p7S3aJklDhFb89Uilh+JQAdchHIxJJX15rMtcRaJOTnSxN9HOPyQv1L778BU\nwJQMUoOS7g2xH9N7az/g87ynEy/kDl3j/LsBWLVRQjqlZP9EArVeQgObMbinD/FR\naxMKrbdZsxnMsbTxsMmRzl9mWS4w0suHQIxd9/XJXJhU8tvlJUfX+v/GDgaWDRQr\naxo4Al8Sk8Yoo5+MhUXmWXj1qgR1xeLbWjCSAzJu6iBBbCt7/Qe3/IiA7XD/KwID\nAQABoyAwHjAOBgNVHQ8BAf8EBAMCB4AwDAYDVR0lAQH/BAIwADANBgkqhkiG9w0B\nAQsFAAOCAQEAlGgAnpg5+VavyvVk//4mMVbYZNtZqRt3oZvXuNGESHdcZ1teGy+Q\nHmRw85Q4iUkdLvE1pmEmcHzWNiNVisnK3PzC9iB2kfGAiWgxmwq418BHVpPIoVds\nULgFT+25rW6slIhbXPlUt7lTCQXKsPxu0z1TuHYW4iR+iw0Szg1uChJ0piBOa2yU\nbzQySAA/1VWxhEXozN1w1VEn1UUBwWg8/dY1s/p4+fFsjjaUPe+jrVGnqxIP4SCF\nbH1xqdYet4yZas1xAZJemKiIc0YkqKvlydhKlObV0lqssyFPpM32WyRPTsxGhcbP\nie/CX1Di+wxp/yqbXK7lhDzFiFzLLQtfUQ==\n-----END CERTIFICATE-----"

@RunWith(AndroidJUnit4ClassRunner::class)
class CryptoTest {
  @Test
  fun test_parseSignatureHeader_ParsesCodeSigningInfo() {
    val codeSigningInfo = Crypto.parseSignatureHeader("sig=\"12345\", keyid=\"test\", alg=\"rsa-v1_5-sha256\"")
    Assert.assertEquals(codeSigningInfo.signature, "12345")
    Assert.assertEquals(codeSigningInfo.keyId, "test")
    Assert.assertEquals(codeSigningInfo.algorithm, Crypto.CodeSigningAlgorithm.RSA_SHA256)
  }

  @Test
  fun test_parseSignatureHeader_DefaultsKeyIdAndAlg() {
    val codeSigningInfo = Crypto.parseSignatureHeader("sig=\"12345\"")
    Assert.assertEquals(codeSigningInfo.signature, "12345")
    Assert.assertEquals(codeSigningInfo.keyId, "root")
    Assert.assertEquals(codeSigningInfo.algorithm, Crypto.CodeSigningAlgorithm.RSA_SHA256)
  }

  @Test(expected = Exception::class)
  @Throws(Exception::class)
  fun test_parseSignatureHeader_ThrowsForInvalidAlg() {
    Crypto.parseSignatureHeader("sig=\"12345\", alg=\"blah\"")
  }

  @Test
  fun test_createAcceptSignatureHeader_CreatesSignatureHeaderDefaultValues() {
    val configuration = Crypto.CodeSigningConfiguration(testCertificate, mapOf())
    val signatureHeader = Crypto.createAcceptSignatureHeader(configuration)
    Assert.assertEquals(signatureHeader, "sig, keyid=\"root\", alg=\"rsa-v1_5-sha256\"")
  }

  @Test
  fun test_createAcceptSignatureHeader_CreatesSignatureHeaderValuesFromConfig() {
    val configuration = Crypto.CodeSigningConfiguration(
      testCertificate,
      mapOf(
        Crypto.CODE_SIGNING_METADATA_ALGORITHM_KEY to "rsa-v1_5-sha256",
        Crypto.CODE_SIGNING_METADATA_KEY_ID_KEY to "test"
      )
    )
    val signatureHeader = Crypto.createAcceptSignatureHeader(configuration)
    Assert.assertEquals(signatureHeader, "sig, keyid=\"test\", alg=\"rsa-v1_5-sha256\"")
  }

  @Test
  fun test_createAcceptSignatureHeader_CreatesSignatureHeaderEscapedValues() {
    val configuration = Crypto.CodeSigningConfiguration(
      testCertificate,
      mapOf(
        Crypto.CODE_SIGNING_METADATA_ALGORITHM_KEY to "rsa-v1_5-sha256",
        Crypto.CODE_SIGNING_METADATA_KEY_ID_KEY to """test"hello\"""
      )
    )
    val signatureHeader = Crypto.createAcceptSignatureHeader(configuration)
    Assert.assertEquals("""sig, keyid="test\"hello\\", alg="rsa-v1_5-sha256"""", signatureHeader)
  }

  @Test(expected = Exception::class)
  @Throws(Exception::class)
  fun test_createAcceptSignatureHeader_ThrowsInvalidAlg() {
    val configuration = Crypto.CodeSigningConfiguration(
      testCertificate,
      mapOf(
        Crypto.CODE_SIGNING_METADATA_ALGORITHM_KEY to "fake",
        Crypto.CODE_SIGNING_METADATA_KEY_ID_KEY to "test"
      )
    )
    Crypto.createAcceptSignatureHeader(configuration)
  }

  @Test
  fun test_verifyCodeSigning_Verifies() {
    val codeSigningConfiguration = Crypto.CodeSigningConfiguration(testCertificate, mapOf())
    val codesigningInfo = Crypto.parseSignatureHeader(testSignature)
    val isValid = Crypto.isSignatureValid(codeSigningConfiguration, codesigningInfo, testBody.toByteArray())
    Assert.assertTrue(isValid)
  }

  @Test
  fun test_verifyCodeSigning_ReturnsFalseWhenSignatureIsInvalid() {
    val codeSigningConfiguration = Crypto.CodeSigningConfiguration(testCertificate, mapOf())
    val codesigningInfo = Crypto.parseSignatureHeader("sig=\"aGVsbG8=\"")
    val isValid = Crypto.isSignatureValid(codeSigningConfiguration, codesigningInfo, testBody.toByteArray())
    Assert.assertFalse(isValid)
  }

  @Test(expected = Exception::class)
  @Throws(Exception::class)
  fun test_verifyCodeSigning_ThrowsWhenKeyDoesNotMatch() {
    val codeSigningConfiguration = Crypto.CodeSigningConfiguration(
      testCertificate,
      mapOf(
        Crypto.CODE_SIGNING_METADATA_KEY_ID_KEY to "test"
      )
    )
    val codesigningInfo = Crypto.parseSignatureHeader("sig=\"aGVsbG8=\", keyid=\"other\"")
    Crypto.isSignatureValid(codeSigningConfiguration, codesigningInfo, testBody.toByteArray())
  }

  @Test(expected = CertificateException::class)
  @Throws(CertificateException::class)
  fun test_construct_multipleCertsInPEMThrowsError() {
    Crypto.CodeSigningConfiguration(
      testCertificate + testCertificate,
      mapOf(
        Crypto.CODE_SIGNING_METADATA_KEY_ID_KEY to "test"
      )
    ).embeddedCertificate
  }

  @Test(expected = CertificateExpiredException::class)
  @Throws(CertificateExpiredException::class)
  fun test_construct_ValidityExpiredThrowsError() {
    Crypto.CodeSigningConfiguration(
      testCertificateValidityExpired,
      mapOf(
        Crypto.CODE_SIGNING_METADATA_KEY_ID_KEY to "test"
      )
    ).embeddedCertificate
  }

  @Test(expected = CertificateException::class)
  @Throws(CertificateException::class)
  fun test_construct_NoKeyUsageThrowsError() {
    Crypto.CodeSigningConfiguration(
      testCertificateNoKeyUsage,
      mapOf(
        Crypto.CODE_SIGNING_METADATA_KEY_ID_KEY to "test"
      )
    ).embeddedCertificate
  }

  @Test(expected = CertificateException::class)
  @Throws(CertificateException::class)
  fun test_construct_NoCodeSigningExtendedKeyUsageThrowsError() {
    Crypto.CodeSigningConfiguration(
      testCertificateNoCodeSigningExtendedKeyUsage,
      mapOf(
        Crypto.CODE_SIGNING_METADATA_KEY_ID_KEY to "test"
      )
    ).embeddedCertificate
  }
}
