import React, {createContext,useState, useContext} from "react";

const FlightResultsContext = createContext<any>(null);


export const FlightResultProvider: React.FC<{children: React.ReactNode}> = ({children})=>{
    const [flightResults,setFlightResults] = useState(null);
    return(
        <FlightResultsContext.Provider value={{flightResults,setFlightResults}}>
            {children}
        </FlightResultsContext.Provider>
    )
}

export default FlightResultsContext;