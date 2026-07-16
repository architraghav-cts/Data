from flask import Flask, request, jsonify

app = Flask(__name__)

students = [{"id": 1, "name": "Archit"}, {"id": 2, "name": "Raghav"}]

@app.route("/students", methods=["GET"])
def get_students():
    return jsonify(students)


@app.route("/students", methods=["POST"])
def add_student():
    data = request.get_json()
    newStudent = {"id": data["id"], "name": data["name"]}
    students.append(newStudent)
    return jsonify(newStudent)

@app.route("/students/<int:id>", methods=["PUT"])
def update_student(id):
    data = request.get_json()
    for student in students:
        if(student["id"]==id):
            student["name"] = data["name"]
            return jsonify({"message":f"user of id {id} updated", "student":student})
            
    return jsonify({"message":"student not found"})

@app.route("/students/<int:id>", methods = ["DELETE"])
def delete_student(id):
    for student in students:
        if(student["id"]==id):
            students.remove(student)
            return jsonify({"message":f"user of id {id} deleted"})
        
    return jsonify({"message":"student not found"}), 404

app.run(debug=True)