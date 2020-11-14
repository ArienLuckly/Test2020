package top.arien.util;


import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

public class test {
	   /**
     * 发送邮件的方法
     *
     * @param to   :收件人
     * @param code :激活码
     */
	static Authenticator auth = new Authenticator(){
	    @Override
	    protected PasswordAuthentication getPasswordAuthentication() {
	    	//参数一：发送邮箱的QQ号 就比如是你发给xxx就写你的qq号  
	    	//参数二：就是刚刚在qq邮箱复制的授权码
	        return new PasswordAuthentication("1045117761@qq.com", "coqebluwymxxbebc");
	    }
	};

	public static void sendMail(String to,int code,String name) {
    	 // TODO Auto-generated method stub
        Properties props = new Properties();
        props.setProperty("mail.smtp.port", "587");
        props.put("mail.smtp.host", "smtp.qq.com");
        props.put("mail.smtp.auth", "true");
      //参数二：发送邮箱的QQ号 就比如是你发给xxx就写你的qq号  
        props.put("mail.from", "1045117761@qq.com");
        Session session = Session.getInstance(props, auth);
        MimeMessage msg = new MimeMessage(session);
        try {
            msg.setFrom();
            msg.setRecipients(Message.RecipientType.TO, to);
            //设置标题
            msg.setSubject("Arien'博客激活邮件");
            //设置时间 --当前时间
            msg.setSentDate(new java.util.Date());
            //设置内容
            msg.setText("<html><body><h1>Email地址验证 账户激活<br><strong>尊敬的："+name+"</strong><br>"+
                    "                    这封信是由Arien's博客发送的。<br>" +
                    "                    如果您是通过Arien's博客的新用户，我们需要对您的地址有效性进行验证以避免垃圾邮件或地址被滥用。<br>" +
                    "                    您的验证码为：<strong style=\"font-size:26px;color:#FF9900\" >" +
                    code+
                    "                   </strong><br>(以上验证码请在注册页面输入，如果您没有注册Arien's博客，请忽略！)<br>" +
                    "                    感谢您的访问，祝您使用愉快！<br>" +
                    "                   </html>", "utf-8", "html");
            //发送
            Transport.send(msg);
            System.out.println("发送完成");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
			
	
	
}

