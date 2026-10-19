package com.example.gestion_opycorp.ui.auth

data class UserAccount(
    val nombre: String,
    val email: String,
    val password: String
)

object UserStore {
    private val accounts = mutableMapOf(
        "ejecutivo@atenas.cl" to UserAccount("Ejecutivo Comercial", "ejecutivo@atenas.cl", "demo123")
    )

    fun registerUser(nombre: String, email: String, password: String): Boolean {
        val normalizedEmail = email.trim().lowercase()
        if (accounts.containsKey(normalizedEmail)) {
            return false // Already registered
        }
        accounts[normalizedEmail] = UserAccount(nombre, normalizedEmail, password)
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
