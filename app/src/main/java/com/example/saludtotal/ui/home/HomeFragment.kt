package com.tuempresa.saludtotal.test.ui.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.tuempresa.saludtotal.test.R
import com.tuempresa.saludtotal.test.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    // Variables de Firebase
    private val auth by lazy { Firebase.auth }
    private val db by lazy { Firebase.firestore }

    // ✅ Variables para Google Sign-In
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var googleSignInLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Configurar cliente de Google
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        // Inicializar el launcher para el resultado de Google
        googleSignInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    val account = task.getResult(ApiException::class.java)!!
                    firebaseAuthWithGoogle(account.idToken!!)
                } catch (e: ApiException) {
                    println("Fallo el inicio de sesión con Google: ${e.statusCode}")
                    Toast.makeText(requireContext(), "Fallo con Google: ${e.statusCode}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Lógica para Email y Contraseña (se mantiene)
        val emailInput = view.findViewById<EditText>(R.id.emailEditText)
        val passwordInput = view.findViewById<EditText>(R.id.passwordEditText)
        val registerBtn = view.findViewById<Button>(R.id.registerButton)
        val loginBtn = view.findViewById<Button>(R.id.loginButton)

        registerBtn.setOnClickListener { /* ... (código anterior) */ }
        loginBtn.setOnClickListener { /* ... (código anterior) */ }

        // ✅ Lógica para el botón de Google
        val googleSignInButton = view.findViewById<Button>(R.id.googleSignInButton)
        googleSignInButton.setOnClickListener {
            googleSignInLauncher.launch(googleSignInClient.signInIntent)
        }
    }

    // ✅ Nueva función para autenticar con Firebase usando el token de Google
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    Toast.makeText(requireContext(), "Login con Google exitoso.", Toast.LENGTH_SHORT).show()

                    // Si es un usuario nuevo, crea su perfil en Firestore
                    if (task.result.additionalUserInfo?.isNewUser == true) {
                        val userId = user?.uid ?: "sin_id_valido"
                        val userProfile = hashMapOf(
                            "role" to "patient",
                            "createdAt" to System.currentTimeMillis(),
                            "email" to user?.email,
                            "displayName" to user?.displayName
                        )
                        db.collection("users").document(userId).set(userProfile)
                            .addOnSuccessListener { println("Perfil de nuevo usuario de Google creado.") }
                    }
                } else {
                    Toast.makeText(requireContext(), "Fallo de autenticación con Firebase.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    // Funciones de registerUser y signInUser (se mantienen sin cambios)
    private fun registerUser(email: String, pass: String) { /* ... (código anterior) */ }
    private fun signInUser(email: String, pass: String) { /* ... (código anterior) */ }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}