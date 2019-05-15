package s2017s40.kr.hs.mirim.mirimitshow;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.regex.Pattern;



public class SignupActivity extends AppCompatActivity {
    EditText name, email, pwd, pwdConfirm, phoneNum1, phoneNum2;
    Spinner phoneSpinner;
    Button signupBtn;
    String nameStr,emailStr,pwdStr,pwdConfirmStr,firstPhone,middlePhone,lastPhone , Phone_num;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        name = findViewById(R.id.signup_name_editText);
        email = findViewById(R.id.signup_email_editText);
        pwd = findViewById(R.id.signup_pass_editText);
        pwdConfirm = findViewById(R.id.signup_passConfirm_editText);
        phoneNum1 = findViewById(R.id.signup_middlePhone_editText);
        phoneNum2 = findViewById(R.id.signup_lastPhone_editText);

        phoneSpinner = findViewById(R.id.signup_phoneNum_Spinner);

        signupBtn = findViewById(R.id.signup_signUp_btn);

        signupBtn.setOnClickListener(new View.OnClickListener() { // onClick 리스너(회원가입 버튼 누르면 동작)
            @Override
            public void onClick(View v) {

                nameStr = name.getText().toString();

                emailStr = email.getText().toString(); //이메일의 아이디 부분

                pwdStr = pwd.getText().toString();
                pwdConfirmStr = pwdConfirm.getText().toString();

                firstPhone = phoneSpinner.getSelectedItem().toString(); //첫 3자리
                middlePhone = phoneNum1.getText().toString(); // 중간 4자리
                lastPhone = phoneNum2.getText().toString(); // 마지막 4자리
                Phone_num = firstPhone + " - " + middlePhone + " - " + lastPhone;

                if(!Confirm()){
                    return;
                }
            }
        });


    }

    public boolean Confirm(){
        //빈 칸이 있는지 검사
        if (nameStr.getBytes().length <= 0 || emailStr.getBytes().length <= 0 ||
                pwdStr.getBytes().length <= 0 || pwdConfirmStr.getBytes().length <= 0 || firstPhone.getBytes().length <= 0 ||
                middlePhone.getBytes().length <= 0 || lastPhone.getBytes().length <= 0) {
            Toast.makeText(getApplicationContext(), "정보를 모두 입력해주십시오.", Toast.LENGTH_SHORT).show();
            return false;
        }
        //비밀번호가 확인비밀번호와 일치하는지 검사
        if(!pwdStr.equals(pwdConfirmStr)){
            Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show();
            return false;
        }

        //휴대폰 번호가 유효한지 검사

        if(!Pattern.matches("^01(?:0|1|[6-9]) - (?:\\d{3}|\\d{4}) - \\d{4}$", Phone_num))
        {
            Toast.makeText(SignupActivity.this,"휴대폰 번호가 올바르지 않습니다.",Toast.LENGTH_SHORT).show();
            return false;
        }

        if(!(emailStr.contains("@") || emailStr.contains(".com"))){
            Toast.makeText(SignupActivity.this,"이메일이 올바르지 않습니다.",Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

}
