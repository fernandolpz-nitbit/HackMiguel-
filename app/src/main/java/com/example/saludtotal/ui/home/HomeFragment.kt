package com.tuempresa.saludtotal.test.ui.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView // <-- Importación necesaria para el enlace de texto
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
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

    // Variables para Google Sign-In
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

        // ✅ LÓGICA FINAL SINCRONIZADA CON EL DISEÑO MODERNO
        val emailInput = view.findViewById<EditText>(R.id.emailEditText)
        val passwordInput = view.findViewById<EditText>(R.id.passwordEditText)
        val continueButton = view.findViewById<Button>(R.id.loginButton) // El botón "Continuar" usa el ID 'loginButton'
        val googleSignInButton = view.findViewById<Button>(R.id.googleSignInButton)
        val registerLink = view.findViewById<TextView>(R.id.registerLinkTextView) // El enlace de texto

        // El botón "Continuar" intenta INICIAR SESIÓN
        continueButton.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()
            if (email.isNotEmpty() && password.isNotEmpty()) {
                signInUser(email, password)
            } else {
                Toast.makeText(requireContext(), "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show()
            }
        }

        // El enlace de texto intenta REGISTRARSE
        registerLink.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()
            if (email.isNotEmpty() && password.length >= 6) {
                registerUser(email, password)
            } else {
                Toast.makeText(requireContext(), "Para registrar, ingresa un email y una contraseña de al menos 6 caracteres.", Toast.LENGTH_LONG).show()
            }
        }

        // El botón de Google se mantiene igual
        googleSignInButton.setOnClickListener {
            googleSignInLauncher.launch(googleSignInClient.signInIntent)
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    Toast.makeText(requireContext(), "Login con Google exitoso.", Toast.LENGTH_SHORT).show()

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
                    findNavController().navigate(R.id.action_login_success_to_main_flow)
                } else {
                    Toast.makeText(requireContext(), "Fallo de autenticación con Firebase.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun registerUser(email: String, pass: String) {
        auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(requireContext(), "Registro exitoso.", Toast.LENGTH_SHORT).show()
                    val userId = auth.currentUser?.uid ?: "sin_id_valido"
                    val userProfile = hashMapOf(
                        "role" to "patient",
                        "createdAt" to System.currentTimeMillis(),
                        "email" to email
                    )
                    db.collection("users").document(userId).set(userProfile)
                        .addOnSuccessListener {
                            println("Perfil de usuario creado. Navegando...")
                            findNavController().navigate(R.id.action_login_success_to_main_flow)
                        }
                        .addOnFailureListener { e -> println("Error al crear perfil: $e") }
                } else {
                    Toast.makeText(requireContext(), "Fallo al registrar: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun signInUser(email: String, pass: String) {
        auth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(requireContext(), "Inicio de sesión exitoso.", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_login_success_to_main_flow)
                } else {
                    Toast.makeText(requireContext(), "Error: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}