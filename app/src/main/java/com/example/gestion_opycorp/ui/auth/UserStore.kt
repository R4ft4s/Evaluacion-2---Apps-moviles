package com.example.gestion_opycorp.ui.auth

data class UserAccount(
    val nombre: String,
    val email: String,
    val password: String,
    val rol: String = "Ejecutivo Comercial"
)

object UserStore {
    private val accounts = mutableMapOf(
        "ejecutivo@atenas.cl" to UserAccount("Joaquín Díez", "ejecutivo@atenas.cl", "demo123", "Ejecutivo Comercial"),
        "supervisor@atenas.cl" to UserAccount("Marco Palma", "supervisor@atenas.cl", "demo123", "Supervisor Comercial"),
        "activaciones@atenas.cl" to UserAccount("Coordinación Activaciones", "activaciones@atenas.cl", "demo123", "Gestión de Activaciones")
    )

    fun registerUser(nombre: String, email: String, password: String, rol: String = "Ejecutivo Comercial"): Boolean {
        val normalizedEmail = email.trim().lowercase()
        if (accounts.containsKey(normalizedEmail)) {
            return false // Already registered
        }
        accounts[normalizedEmail] = UserAccount(nombre, normalizedEmail, password, rol)
        return true
    }

    fun authenticate(email: String, password: String): UserAccount? {
        val normalizedEmail = email.trim().lowercase()
        val user = accounts[normalizedEmail]
        if (user != null && user.password == password) {
            return user
        }
        return null
    }
}
