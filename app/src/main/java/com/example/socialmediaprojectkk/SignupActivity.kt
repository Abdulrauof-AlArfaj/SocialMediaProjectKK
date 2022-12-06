package com.example.socialmediaprojectkk

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.socialmediaproject.APIClient
import com.example.socialmediaproject.APIinterface
import com.example.socialmediaprojectkk.databinding.ActivitySignupBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.util.regex.Pattern

class SignupActivity : AppCompatActivity() {

    lateinit var binding: ActivitySignupBinding
    lateinit var users:ArrayList<UserItem>
    lateinit var context: Context
    val EMAIL_ADDRESS_PATTERN: Pattern = Pattern.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
            "\\@" +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
            "(" +
            "\\." +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
            ")+")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //These variables to check if the name, email, and password are valid:
        var validName=true
        var validEmail=true
        var validPassword=true
        //Get users inorder to check later if user name or password exist or not
        users= arrayListOf()
        val apiInterface = APIClient().getClinet()?.create(APIinterface::class.java)
        apiInterface?.getUsers()?.enqueue(object: Callback<User>{
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.body()!=null)
                { users= response.body()!!}
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.d("MAIN", "ISSUE")
            }

        })
        //===============================================================================
        binding.apply {
            signupBtn.setOnClickListener{

                var name= nameEt.text.toString()
                var userPassWord=passEt.text.toString()
                var confirmPassword=passConfEt.text.toString()
                var email=emailEt.text.toString()
                //Checking constraints:

                //Empty constrain:
                if (name=="" ||userPassWord==""||email=="")
                {
                    Toast.makeText(this@SignupActivity,"Please fill all fields",Toast.LENGTH_SHORT).show()
                     validName=false
                     validEmail=false
                     validPassword=false
                }
                //--------------------------------------------------------------
                //Check if the user name/email used before or not
                else{
                    validName=newUser(name,email)

                //--------------------------------------------------------------
                //Check if the email pattern and length
                if (!checkEmailConstrain(email)||email.length>64){
                    emailConstrain.text="the email must be as: address@organization.etension"
                    emailConstrain.visibility=View.VISIBLE
                    emailConstrain.setTextColor(Color.RED)
                    validEmail=false
                }//end checking email pattern
                else{emailConstrain.visibility=View.INVISIBLE
                    validEmail=true
                }


                //--------------------------------------------------------------
                //Check if the password valid or not
                //--------------------------------------------------------------
                    validPassword=checkPassword(userPassWord)&&match(userPassWord,confirmPassword)
                //Check if all data are valid, then we can add the new user
                    if (validName&&validEmail&&validPassword){
                        var currentTime = LocalDateTime.now()


                        var newUser=UserItem("",currentTime.toString(),email,0,"",name,"",userPassWord)

                        apiInterface?.addUser(newUser)?.enqueue(object :
                            Callback<UserItem> {
                            override fun onResponse(call: Call<UserItem>, response: Response<UserItem>) {

                                Toast.makeText(this@SignupActivity, "The user added successfully", Toast.LENGTH_SHORT).show()
                                recreate()
                                var intent= Intent(context,LoginActivity::class.java)
                                context.startActivity(intent)
                            }

                            override fun onFailure(call: Call<UserItem>, t: Throwable) {
                                Toast.makeText(this@SignupActivity, "Sorry something goes wrong!", Toast.LENGTH_LONG).show()
                            }
                        })

                    }//end if valid
                }//else (Not empty)





            }//End of onClickListener

        }//End of binding.apply




    }

//-------------------------------------------------------------------


    //Constrains functions:
    fun newUser(name:String,email:String):Boolean{
        for (oldUser in users) {
            if (oldUser.username == name) {

                binding.apply {
                    nameConstrain.text="This user is already exists"
                    nameConstrain.visibility=View.VISIBLE
                    nameConstrain.setTextColor(Color.RED)

                }
                return false
            }//end of check existing user name

            if (oldUser.email==email) {
                Toast.makeText(
                    this@SignupActivity,
                    "This email is already exists",
                    Toast.LENGTH_SHORT
                ).show()
                binding.apply {
                    emailConstrain.setTextColor(Color.RED)
                    emailConstrain.text= "This email already used"
                    emailConstrain.visibility=View.VISIBLE
                    return false}
            }//end of check existing email

        }//End of for
        //In case the user not exist so return the text to invisible state
        binding.apply {
            nameConstrain.visibility=View.INVISIBLE
            emailConstrain.visibility=View.INVISIBLE


        }
        return true
    }
    //=====================================================================

    private fun checkEmailConstrain(email: String): Boolean {
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches()
        //=====================================================================

    }//End of checkEmailConstrain

    fun checkPassword(password: String):Boolean{
        if(hasUpper(password)&&hasLower(password)&&(hasNumber(password))){
            binding.apply {
                passwordConstrain.visibility= View.INVISIBLE
            }
            return true}
        else{
//            Toast.makeText(this@SignupActivity,"The password must includes Uppercase litter, Lowercase litter, number, and special characters",
//                Toast.LENGTH_LONG).show()

            binding.apply {
                passwordConstrain.text="The password must includes Uppercase litter, Lowercase litter, number"
                passwordConstrain.visibility= View.VISIBLE
                passwordConstrain.setTextColor(Color.RED)
            }

        }


        return false
    }

    private fun hasUpper(text: String): Boolean{
        for (i in 0..text.length-1){
            if (text[i]==text[i].toUpperCase())
            {
                return true
            }
        }
        return false
    }

    private fun hasLower(text: String): Boolean{
        for (i in 0..text.length-1){
            if (text[i]==text[i].toUpperCase())
            {
                return true
            }
        }
        return false
    }

    private fun hasNumber(text: String): Boolean{
        for(i in 0..9){
            if(text.contains(i.toString())){
                return true
            }
        }
        return false
    }

    fun match(password: String,password2: String):Boolean
    {
        if(password==password2){
            binding.apply {
                passConfEt.setTextColor(Color.BLACK)
            }
            return true

        }
        else
        {
            Toast.makeText(this@SignupActivity,"The password must match its confirm",Toast.LENGTH_SHORT).show()
            binding.apply {
                passConfEt.setTextColor(Color.RED)
            }
        }
            return false
    }



}