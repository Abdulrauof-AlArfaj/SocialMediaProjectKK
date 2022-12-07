package com.example.socialmediaprojectkk


import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.socialmediaprojectkk.API.APIClient
import com.example.socialmediaprojectkk.API.APIinterface
import com.example.socialmediaprojectkk.Data.User
import com.example.socialmediaprojectkk.Data.UserItem
import com.example.socialmediaprojectkk.databinding.ActivityLoginBinding
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URL

class LoginActivity : AppCompatActivity() {
    lateinit var users: ArrayList<UserItem>
    var apiKey = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        var context: Context
        var found = false


        super.onCreate(savedInstanceState)
        var binding: ActivityLoginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //-------------------------------------------------
        //Get users inorder to check if user name and password exist and match
        users = arrayListOf()
        val apiInterface = APIClient().getClinet()?.create(APIinterface::class.java)
        apiInterface?.getUsers()?.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.body() != null) {
                    users = response.body()!!
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.d("MAIN", "ISSUE")
            }

        })

        //set action to sign up text to go to sign up activity
        context = this@LoginActivity
        binding.apply {
            signupBtn.setOnClickListener {
                var intent = Intent(context, SignupActivity::class.java)
                context.startActivity(intent)

            }//end of on click-listener for signup
            loginBtn.setOnClickListener {
                found=false
                var userName = nameEt.text.toString()
                var userPassword = passEt.text.toString()
                if (userName != "" && userPassword != "") {
                    for (user in users) {
                        //passEt.setTextColor(Color.BLACK)//return the text color to black if the user change user name
                        if (userName == user.username || userName == user.email) {//Accept email for more flexibility of program
                            //Toast.makeText(this@LoginActivity, "Here we are to check user password", Toast.LENGTH_LONG).show()
                            userName = user.username//So if the user enter his email, then we convert the value to user name

                            found = true
                            //Try to get API key
                            var API_Key=""
                            CoroutineScope(Dispatchers.IO).launch {

                                var data = async {

                                    fetchData("https://apidojo.pythonanywhere.com/login/$userName/$userPassword")

                                }.await()//TO waite till the data fetched, then execute the following lines
                                if (data.isNotEmpty()) {

                                    withContext(Dispatchers.Main)//This to write in main layout in text view
                                    {

                                        API_Key = data
                                        if (API_Key != "Unable to log in"&& API_Key !=""){
                                            var intent = Intent(context, MainActivity::class.java)
                                            intent.putExtra("API_Key", API_Key)
                                            context.startActivity(intent)
                                        }
                                        else{
                                            Toast.makeText(
                                                this@LoginActivity,
                                                "Some things wrong! Try to enter correct user name and password",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }//end else block

                                    }//end of main scope

                                }



                            }//End of CoroutineScope



                        }//If the user founded


                    }//End of for loop
                    if (!found) {
                        Toast.makeText(
                            this@LoginActivity,
                            "The user name Not founded",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                }//End block where the fields is not empty
                else {
                    Toast.makeText(
                        this@LoginActivity,
                        "Please enter user name and password",
                        Toast.LENGTH_LONG
                    ).show()
                }


            }//end loginBtn.setOnClickListener

        }//end binding.apply


    }//End of onCreate function

//    fun getAPI(url: String):String {
//        var response = ""
//
//            CoroutineScope(Dispatchers.IO).launch {
//
//                var data = async {
//
//                    fetchData(url)
//
//                }.await()//TO waite till the data fetched, then execute the following lines
//                if (data.isNotEmpty()) {
//                    response=data
//                }
//
//            }
//
//            return response
//
//    }//end of function

    //=================================
    fun fetchData(url: String):String{//This function response for fetching the data:

        var response=""
        try {
            response = URL(url).readText(Charsets.UTF_8)

        }catch (e:Exception)
        {
            println("Error $e")

        }
        return response

    }



}//End of the class