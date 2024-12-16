import {FormEvent, useState} from "react";


type LoginPageProps = {
    login: (username:string, password:string)=>void;
}

function LoginPage(props:Readonly<LoginPageProps>) {
    const [username, setUsername] = useState<string>("");
    const [password, setPassword] = useState<string>("");

    function handleSubmit(event: FormEvent<HTMLFormElement>) {
        event.preventDefault();
        props.login(username, password);
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