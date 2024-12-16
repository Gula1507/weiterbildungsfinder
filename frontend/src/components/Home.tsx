import {Organization} from "../types/Organization.ts";
import OrganizationGallery from "./OrganizationGallery.tsx";
import "../styles/Home.css"
import {useNavigate} from "react-router-dom";
import {useState} from "react";

type HomeProps = {
    organizations: Organization[];
    loading: boolean;
    loadOrganizations: (page: number, size: number, searchText: string) => void;
    totalPages: number;
    searchText: string;
    setSearchText: (text: string) => void;

};

function Home(props: HomeProps) {
    const navigate = useNavigate();

    const [page, setPage] = useState<number>(0);
    const [size] = useState<number>(10);
    const [localSearchText, setLocalSearchText] = useState<string>(props.searchText);
    const handleAddOrganization = () => {
        navigate("add-organization");
    };


    const handleSearchSubmit = () => {
        props.loadOrganizations(0, size, localSearchText);
        setPage(0);
        props.setSearchText(localSearchText);
    };

    const handlePageChange = (newPage: number) => {
        if (newPage >= 0 && newPage < props.totalPages) {
            setPage(newPage);
            props.loadOrganizations(newPage, size, props.searchText);
        }
    };

    return (
        <>
            <div className="slogan-container">
                <img
                    className="small-image"
                    src="/images/home-icons.png"
                    alt="Icons representing home features such as house, magnifying glass, thumbs up"
                />
                <div className="slogan">
                    <h2>Entdecke KURSDA</h2>
                    <p>
                        Finde heraus, welche Weiterbildungsmöglichkeiten es in deiner Region gibt – übersichtlich und
                        einfach.
                        Hol dir neue Fähigkeiten, bleib motiviert und gestalte deine Zukunft aktiv – jetzt und für die
                        kommenden Jahre.
                    </p>
                </div>
            </div>

            <div className="home-container">
                {props.loading ? (
                    <div className="loading-indicator">Lade Weiterbildungsanbieter...</div>
                ) : (
                    <>
                        <input
                            type="text"
                            value={localSearchText}
                            onChange={(e) => setLocalSearchText(e.target.value)}
                            placeholder="Nach einem Weiterbildungsanbieter suchen"
                        />
                        <button onClick={handleSearchSubmit}>Suchen</button>


                        {props.organizations.length > 0 ? (
                            <OrganizationGallery organizations={props.organizations}/>
                        ) : (
                            "Keine Weiterbildungsanbieter gefunden"
                        )}

                        <button className="add" onClick={handleAddOrganization}>
                            + NEU
                        </button>
                    </>
                )}

                <div className="pagination">
                    <button onClick={() => handlePageChange(0)} disabled={page <= 0}>⏮️</button>
                    <button onClick={() => handlePageChange(page - 1)} disabled={page <= 0}>🢦</button>
                    <span>Seite {page + 1} von {props.totalPages}</span>
                    <button onClick={() => handlePageChange(page + 1)} disabled={page >= props.totalPages - 1}>
                        🢧
                    </button>
                    <button onClick={() => handlePageChange(props.totalPages - 1)}
                            disabled={page >= props.totalPages - 1}>⏭️
                    </button>
                </div>
            </div>
        </>
    );
}

export default Home;