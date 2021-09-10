package com.bazzi.job.platform.service;

import org.springframework.data.util.Pair;

import java.util.List;
import java.util.Map;

public interface EmailService {
    /**
     * 发送邮件，基于ftl，不包含附件
     *
     * @param sendTo   收件人
     * @param title    邮件标题
     * @param template flt路径
     * @param content  ftl内容
     * @throws Exception 异常
     */
    void sendTemplateMail(String[] sendTo, String title, String template, Map<String, Object> content) throws Exception;

    /**
     * 发送邮件，基于ftl，有附件
     *
     * @param sendTo      收件人
     * @param title       邮件标题
     * @param template    flt路径
     * @param content     ftl内容
     * @param attachments 附件
     * @throws Exception 异常
     */
    void sendTemplateMail(String[] sendTo, String title, String template, Map<String, Object> content,
                          List<Pair<String, byte[]>> attachments) throws Exception;
}
