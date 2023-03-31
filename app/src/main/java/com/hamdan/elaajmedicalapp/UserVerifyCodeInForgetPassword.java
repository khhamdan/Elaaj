package com.hamdan.elaajmedicalapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.chaos.view.PinView;

import java.util.Properties;
import java.util.Random;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class UserVerifyCodeInForgetPassword extends AppCompatActivity {
    String stringNumbers, emailOfUser;
    String pinViewNumbers;
    PinView pin_view;
    AppCompatButton sendCode_btn, verifyCode_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verify_code_in_forget_password);

        pin_view = findViewById(R.id.pin_view);
        sendCode_btn = findViewById(R.id.sendCode_btn);
        verifyCode_btn = findViewById(R.id.verifyCode_btn);

        Toolbar toolbar = findViewById(R.id.toolbar_verify_code);
        toolbar.setTitle("Verify Code");
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.black));
        setSupportActionBar(toolbar);

        //4-Making the home button in toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_arrow);

        Bundle bundle = getIntent().getExtras();
        emailOfUser = bundle.getString("email");

        sendCode_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //6 digit random numbers for verification
                Random random = new Random();
                int number = random.nextInt(999999);
                stringNumbers = String.format("%06d", number);
                sendEmail(emailOfUser);

                Toast.makeText(UserVerifyCodeInForgetPassword.this, "Code has been sent to your email", Toast.LENGTH_LONG).show();
            }
        });


        verifyCode_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pinViewNumbers = pin_view.getText().toString().trim();
                if (pinViewNumbers.equals(stringNumbers)) {
                    Intent intent = new Intent(UserVerifyCodeInForgetPassword.this, UserSetNewPassword.class);
                    intent.putExtra("email", emailOfUser);
                    startActivity(intent);
                    startActivity(intent);
                } else {
                    Toast.makeText(UserVerifyCodeInForgetPassword.this, "Verification Code is wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void sendEmail(String userEmailInput) {
        try {
            String stringSenderEmail = "f2018-443@bnu.edu.pk";
            String stringReceiverEmail = userEmailInput;
            String stringPasswordSenderEmail = "hamsab123";

            String stringHost = "smtp.gmail.com";

            Properties properties = System.getProperties();

            properties.put("mail.smtp.host", stringHost);
            properties.put("mail.smtp.port", "465");
            properties.put("mail.smtp.ssl.enable", "true");
            properties.put("mail.smtp.auth", "true");

            javax.mail.Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(stringSenderEmail, stringPasswordSenderEmail);
                }
            });

            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(stringReceiverEmail));

            mimeMessage.setSubject("Subject: Elaaj Forget Password");
            mimeMessage.setText("Hello User, \n\n you verification code is " + stringNumbers + ". \n\n Regards !\nElaaj");

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Transport.send(mimeMessage);
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();

        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

}