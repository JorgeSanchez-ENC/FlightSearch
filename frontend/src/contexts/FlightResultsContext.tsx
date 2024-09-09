import React, {createContext,useState} from "react";

const FlightResultsContext = createContext<any>(null);


export const FlightResultProvider: React.FC<{children: React.ReactNode}> = ({children})=>{
    const [flightResults,setFlightResults] = useState(null);
    const [selectedFlight, setSelectedFlight] = useState(null);


    return(
        <FlightResultsContext.Provider value={{flightResults,setFlightResults, selectedFlight, setSelectedFlight}}>
            {children}
        </FlightResultsContext.Provider>
    )
}

export default FlightResultsContext;