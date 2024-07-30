<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
<title>Firebase Auth Example</title>
<script type="module" src="/resource/firebase.js"></script>
</head>
<body>
	<body>
    <h1>Firebase Authentication</h1>

    <button onclick="googleSignIn()">Sign in with Google</button>

    <h2>Sign Up</h2>
    <input type="email" id="signup-email" placeholder="Email" required>
    <input type="password" id="signup-password" placeholder="Password" required>
    <button onclick="signupEmail()">Sign Up</button>

    <h2>Login</h2>
    <input type="email" id="login-email" placeholder="Email" required>
    <input type="password" id="login-password" placeholder="Password" required>
    <button onclick="loginEmail()">Login</button>

    <div id="user-info"></div>
</body>
</html>