package arolla.aptitudes

data class Skill(val name: String, val level: Int)
data class Employee(val name: String, val skills: Collection<Skill>)
