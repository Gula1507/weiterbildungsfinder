import {Link, useLocation} from "react-router-dom";
import {AppUser} from "../types/AppUser.ts";

type HeaderProps = {
    appUser: AppUser | null | undefined;
    logout: () => void;
}


function Header(props: HeaderProps) {

    const location = useLocation();
    if (location.pathname === "/") return null;

    return (
        <div>
            <nav>
                <div className="link-container">
                    <Link to={"/"} className="link">Startseite</Link>
                    {!props.appUser && (
                        <>
                            <Link to={"/login"} className="link">Login</Link>
                            <Link to={"/register"} className="link">Registrieren</Link>
                        </>
                    )}
                    {props.appUser && (
                        <Link to="#" className="link" onClick={props.logout}>Logout</Link>
                    )}

                </div>
            </nav>
        </div>
    );
}

export default Header;