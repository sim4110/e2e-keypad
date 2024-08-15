import React, { useEffect, useState } from 'react';
import './App.css';
import axios from 'axios';

function App() {
    const [keypadItems, setKeypadItems] = useState([]);
    const [loading, setLoading] = useState(true);
    const [clickCount, setClickCount] = useState(0);
    const [clickedHashes, setClickedHashes] = useState([]);

    // Function to generate a new keypad
    const generateNewKeypad = () => {
        setLoading(true);
        setKeypadItems([]);
        axios.post('/api/keypad')
                    .then((response) => {
                        const keypadId = response.data.keypadId;
                        axios.get(`/api/keypad?keypadId=${keypadId}`)
                            .then((response) => {
                                setKeypadItems(response.data.keypadItems);
                                setLoading(false);
                                setClickCount(0);
                                setClickedHashes([]);
                            })
                            .catch((error) => {
                        console.error("Error fetching keypad data", error);
                        setLoading(false);
                    });
            })
            .catch((error) => {
                console.error("Error creating new keypad", error);
                setLoading(false);
            });
    };

    useEffect(() => {
        generateNewKeypad();
    }, []);

    if (loading) {
        return <div>Loading...</div>;
    }

    const handleButtonClick = (item) => {
        // Ignore "blank" buttons
        if (item.number === "blank") {
            return;
        }

        console.log(`Hash for number ${item.number}: ${item.randomString}`);

        setClickCount(prevCount => prevCount + 1);
        setClickedHashes(prevHashes => [...prevHashes, item.randomString]);

        if (clickCount + 1 === 6) {
            setTimeout(() => {
                alert(`Clicked button hashes:\n${[...clickedHashes, item.randomString].join('\n')}`);
                generateNewKeypad();
            }, 100);
        }
    };

    // Function to render circles that indicate click status
    const renderClickCircles = () => {
        return (
            <div className="click-indicator">
                {Array.from({ length: 6 }).map((_, index) => (
                    <div
                        key={index}
                        className={`circle ${index < clickCount ? 'active' : ''}`}
                    ></div>
                ))}
            </div>
        );
    };

    const renderKeypadRows = () => {
        const rows = [];
        for (let i = 0; i < keypadItems.length; i += 4) {
            rows.push(
                <tr key={i}>
                    {keypadItems.slice(i, i + 4).map((item, index) => (
                        <td key={index}>
                            <button
                                className="keypad-button"
                                onClick={() => handleButtonClick(item)}
                            >
                                {item.number === "blank" ? "" : item.number}
                            </button>
                        </td>
                    ))}
                </tr>
            );
        }
        return rows;
    };

    return (
        <div className="app">
            {renderClickCircles()}
            <table className="keypad-table">
                <tbody>
                {renderKeypadRows()}
                </tbody>
            </table>
        </div>
    );
}

export default App;
