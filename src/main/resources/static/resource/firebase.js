import { initializeApp } from "https://www.gstatic.com/firebasejs/9.2.0/firebase-app.js";
import { getAuth, signInWithPopup, GoogleAuthProvider } from "https://www.gstatic.com/firebasejs/9.2.0/firebase-auth.js";

const firebaseConfig = {
	apiKey: "AIzaSyCL3nZuXW_hHpstU9JIe3KAeDyEiDXI9RU",
	authDomain: "project-15c4f.firebaseapp.com",
	projectId: "project-15c4f",
	storageBucket: "project-15c4f.appspot.com",
	messagingSenderId: "157771801307",
	appId: "1:157771801307:web:25729f5c4241aa1a3f62fe"
};

// firebase 시작
const app = initializeApp(firebaseConfig);
const auth = getAuth(app);

// 구글 로그인
window.googleSignIn = async function() {
	const provider = new GoogleAuthProvider();
	try {
		const result = await signInWithPopup(auth, provider);
		const user = result.user;
		
		console.log(user);
		firebaseLogin(user.uid, user.email);
	} catch (error) {
		console.error("Google 로그인 오류:", error.message);
	}
}