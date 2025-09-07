package com.tuempresa.saludtotal.test.ui.register // o la ruta que corresponda

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.tuempresa.saludtotal.test.R
import com.tuempresa.saludtotal.test.databinding.FragmentRegisterBinding // <-- ¡Importante! Cambia a RegisterBinding

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    // Variables de Firebase
    private val auth by lazy { Firebase.auth }
    private val db by lazy { Firebase.firestore }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.registerButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()
            val confirmPassword = binding.confirmPasswordEditText.text.toString().trim()

            // Validaciones
            if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(requireContext(), "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password.length < 6) {
                Toast.makeText(requireContext(), "La contraseña debe tener al menos 6 caracteres.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password != confirmPassword) {
                Toast.makeText(requireContext(), "Las contraseñas no coinciden.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Si todo es válido, registrar al usuario
            registerUser(email, password)
        }

        binding.loginLinkTextView.setOnClickListener {
            // Regresar a la pantalla de login
            findNavController().popBackStack()
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
                            // Navegar al flujo principal de la app
                            findNavController().navigate(R.id.navigation_dashboard)
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(requireContext(), "Error al crear perfil: ${e.message}", Toast.LENGTH_LONG).show()
                        }
                } else {
                    Toast.makeText(requireContext(), "Fallo al registrar: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}