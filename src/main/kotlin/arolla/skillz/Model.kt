package arolla.skillz

data class Skill(val name: String)
data class Employee(val name: String, val skills: Collection<Skill>)
