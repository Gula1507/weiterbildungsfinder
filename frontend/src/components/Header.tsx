import {Link, useLocation} from "react-router-dom";

function Header() {

    const location = useLocation();
    if (location.pathname === "/") return null;

    return (
        <div>
            <nav>
                <Link to={"/"}>Startseite</Link>
            </nav>
        </div>
    );
}

export default Header;