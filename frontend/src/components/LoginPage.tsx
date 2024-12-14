import axios from "axios";
import {FormEvent, useState} from "react";
import {useNavigate} from "react-router-dom";


function LoginPage() {
    const [username, setUsername] = useState<string>("");
    const [password, setPassword] = useState<string>("");
    const navigate = useNavigate();

    function login() {
        axios.post("/api/users/login", {}, {
            auth: {
                username: username,
                password: password
            }
        })
            .then(() => {
                setPassword("");
                setUsername("");
                navigate("/")
            })
            .catch(e => {
                setPassword("");
                console.error(e)
            })
    }

    function handleSubmit(event: FormEvent<HTMLFormElement>) {
        event.preventDefault();
        login();
    }

    return (
        <form onSubmit={handleSubmit}>
            <input placeholder="Benutzername" value={username} onChange={
                (e) => setUsername(e.target.value)}/>
            <input placeholder="Passwort" type="password" value={password} onChange={
                (e) => setPassword(e.target.value)
            }/>
            <button>Login</button>
        </form>
    );
}

export default LoginPage;