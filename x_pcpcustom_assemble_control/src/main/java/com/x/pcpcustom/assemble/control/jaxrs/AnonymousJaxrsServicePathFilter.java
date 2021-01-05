package com.x.pcpcustom.assemble.control.jaxrs;

import com.x.base.core.project.jaxrs.AnonymousCipherManagerUserJaxrsFilter;

import javax.servlet.annotation.WebFilter;

/**
 * web服务过滤器，无需登录及可访问
 */
@WebFilter(urlPatterns = {
        "/jaxrs/pcp/*",
        "/jaxrs/pcpserv/*",
}, asyncSupported = true )
public class AnonymousJaxrsServicePathFilter extends AnonymousCipherManagerUserJaxrsFilter {
}
