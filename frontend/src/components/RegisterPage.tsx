import {FormEvent, useState} from "react";
import {useNavigate} from "react-router-dom";
import axios from "axios";


function RegisterPage() {

    const [username, setUsername] = useState<string>("");
    const [password, setPassword] = useState<string>("");
    const navigate = useNavigate();

    function register() {
        axios.post("api/users/register", {
            "username": username,
            "password": password

        })
            .then(() => {
                setPassword("");
                setUsername("");
                console.log("Registration successfully")
                navigate("/")
            })
            .catch(e => {
                setUsername("");
                setPassword("");
                console.error(e)
            })
    }

    function handleSubmit(event: FormEvent<HTMLFormElement>) {
        event.preventDefault();
        register();
    }

    return (
        <form onSubmit={handleSubmit}>
            <input placeholder="Benutzername" value={username} onChange={
                (e) => setUsername(e.target.value)}/>
            <input placeholder="Passwort" type="password" value={password} onChange={
                (e) => setPassword(e.target.value)
            }/>
            <button>Registriere dich</button>
        </form>
    );
}

export default RegisterPage;