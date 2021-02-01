package com.liebe.base_lib.network.ssl;

import javax.net.ssl.HostnameVerifier;

/**
 * Trusted all host name verifier.
 *
 * @author liangZe
 */
public class TrustedHostnameVerifier {
    private static HostnameVerifier sTrustedVerifier;

    public static HostnameVerifier getTrustedVerifier() {
        if (sTrustedVerifier == null) {
            sTrustedVerifier = (hostname, session) -> true;
        }
        return sTrustedVerifier;
    }
}
