import {FormEvent, useState} from "react";
import {useLocation, useNavigate} from "react-router-dom";
// import {useLocation, useNavigate} from "react-router-dom";


type LoginPageProps = {
    login: (username:string, password:string)=>void;
}

function LoginPage(props:Readonly<LoginPageProps>) {
    const [username, setUsername] = useState<string>("");
    const [password, setPassword] = useState<string>("");

    const location = useLocation();
    const navigate = useNavigate();
    // const from = location.state?.from || '/';




    function handleSubmit(event: FormEvent<HTMLFormElement>) {
        event.preventDefault();
        props.login(username, password);
        // navigate(-1);
        const redirectPath = location.state?.from || "/";
        navigate(redirectPath);
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