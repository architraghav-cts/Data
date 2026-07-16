# print("Hello, Python")

# name = input("Enter the name: ")
# age = int(input("Enter the age: "))

# # print("My name is",name,"and age is",age,".");

# a = 10
# b = 20
# print("a ->", a, "and b ->", b)
# a = a + b
# b = a - b
# a = a - b
# print("a ->", a, "and b ->", b)

# number = int(input("Enter a number: "))
# if number % 2 == 0:
#     print("Even")
# else:
#     print("Odd")

# number = int(input("Enter a number: "))
# if number > 0:
#     print("Positive Number")
# elif number < 0:
#     print("Negative Number")
# else:
#     print("Zero")

# number1 = int(input("Enter a number1: "))
# number2 = int(input("Enter a number2: "))
# number3 = int(input("Enter a number3: "))
# if number1 > number2 and number1 > number3:
#     print(number1)
# elif number2 > number3:
#     print(number2)
# else:
#     print(number3)

# number = int(input("Enter a number: "))
# factorial = 1
# for i in range(1, number + 1):
#     factorial = factorial * i
# print(factorial)

# number = int(input("Enter a number: "))
# for i in range(1, 11):
#     print(f"{number}*{i}={number*i}")

# number = abs(int(input("Enter a number: ")))
# sum = 0
# while number > 0:
#     ld = number % 10
#     sum = sum + ld
#     number = number // 10

# print(sum)

# number = abs(int(input("Enter a number: ")))
# originalNumber = number
# reverse = 0
# while number > 0:
#     ld = number % 10
#     reverse = (reverse * 10) + ld
#     number = number // 10

# print(reverse)

# for i in range(1, 101):
#     if i % 2 == 0:
#         print(i)

# number = int(input("Enter a number: "))
# a = 0
# b = 1
# print(a)
# print(b)
# for i in range(2, number):
#     c = a + b
#     print(c)
#     a = b
#     b = c

# number = int(input("Enter the number: "))
# flag = 0
# if number <= 1:
#     print("Not Prime")
#     flag = 1
# for i in range(2, number):
#     if number % i == 0:
#         flag = 1

# if flag == 0:
#     print("Prime")
# else:
#     print("Not Prime")

# number = abs(int(input("Enter a number: ")))
# originalNumber = number
# reverse = 0
# while number > 0:
#     ld = number % 10
#     reverse = (reverse * 10) + ld
#     number = number // 10

# if originalNumber == reverse:
#     print("Pallindrome")
# else:
#     print("Not pallindrome")

# str = input("Enter the string: ").lower()
# vcount = 0
# ccount = 0
# for i in str:
#     if i in "aeiou":
#         vcount += 1

#     elif i.isalpha():
#         ccount += 1

# print(f"Vowels count: {vcount} and Consonants count: {ccount}")

# for i in range(1, 6):
#     for j in range(1, i + 1):
#         print("*", end=" ")
#     print()

# li = [1, 2, 3, 4, 5, "Archit"]
# for i in range(len(li)):
#     print(li[i])


# print(dir(list))
# li.append(6)
# li.insert(5, 6)
# li.extend([7, 8, 9])
# li.remove("Archit")
# li.count(2)
# li.pop(2)
# li.reverse()
# li.sort()
# print(li.index(1))
# li.clear()

# print(li)

# for i in li:
#     print(i)

# li = [-10, -909, 0, -45, -67, -98, -223]
# largest = li[0]
# smallest = li[0]
# for i in li:
#     if largest < i:
#         largest = i
#     if smallest > i:
#         smallest = i
# print(largest)
# print(smallest)

# li = [-10, -909, 0, -45, -67, -98, -223]
# largest = li[0]
# slargest = li[0]
# for i in li:
#     if largest < i:
#         largest = i
#     elif slargest < i and slargest != largest:
#         slargest = i

# print(largest, slargest)
# li.sort()
# print(li)

# tup = (1, 2, 3, 4, 5, 5, 5, 5)
# print(type(tup))

# for i in tup:
#     print(i)

# a = (1,)
# print(type(a))

# a, b, c, d = (1, 2, 3, 4)
# print(a, b, c, d)

# print(tup.index(2))
# print(tup.count(5))

# li = list(tup)
# print(type(li))

# s = {1, 2, 3, 4, 5}
# b = {4, 5, 6, 7, 8}

# for i in s:
#     print(i)

# s.remove(10)
# s.discard(10)
# s.pop()
# s.clear()
# s.add(6)

# a = s.union(b)
# print(s | b)

# a = s.intersection(b)
# print(s & b)

# a = s.difference(b)
# print(s - b)

# a = s.symmetric_difference(b)
# print(s ^ b)


# def myName(name):
#     print(name)

# myName("Archit Raghav")

# def add(a, b, c):
#     print(a + b + c)

# add(1, 4, 5)

# def add2(a, b, c):
#     print(a + b + c)

# add2(a=2, c=1, b=1)

# def add3(a=10, b=89):
#     print(a + b)

# add3(12, 12)

# def str():
#     return "Hi"

# print(str())

# def isPallindrome(str):
#     newRev = ""
#     for i in range(len(str) - 1, -1, -1):
#         newRev = newRev + str[i]

#     print(str)
#     print(newRev)

#     if str == newRev:
#         print("Pallindrome")
#     else:
#         print("Not a Pallindrome")

# isPallindrome("NamaN")

# d = {10: 100, 20: 200, 30: 300, 40: 400}
# for i in d:
#     print(i)
#     print(d[i])

# for i in d.values():
#     print(i)

# for i in d.keys():
#     print(i)

# print(d.items())

# number = int(input("Enter the number: "))
# try:
#     print(number / 10)
# except Exception as err:
#     print(err)
# else:
#     print("All ok")
# finally:
#     print("I don't care.")
    
# class Animal:
#     def __init__(self, name):
#         self.name = name
        
# cat = Animal("Ron")


# def sum(a,b):
    
#     print(a+b)
    
# sum(1,2)

a = open("f.txt")
b = open("h.txt", "w")
b.write("hello" )
b.close()
# print(a.read())

