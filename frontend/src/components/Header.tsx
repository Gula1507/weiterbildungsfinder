import {useNavigate} from "react-router-dom";


function Header() {

    const navigate = useNavigate();

    const handleAddOrganization = () => {
        navigate("/add-organization")
    }

    return (
        <div>
            <button onClick={handleAddOrganization}>Weiterbildungsanbieter hinzufügen</button>
        </div>
    );
}

export default Header;