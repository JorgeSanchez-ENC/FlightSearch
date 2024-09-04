import React, { useContext } from "react";
import { List, Card, Row, Col, Typography, Divider, Flex } from "antd";
import FlightResultsContext from "../../contexts/FlightResultsContext";
import { useNavigate } from "react-router-dom";
import "./index.css";
import dayjs from "dayjs";

const { Title, Paragraph} = Typography;

const Results: React.FC = () => {
    const { flightResults } = useContext(FlightResultsContext);
    const {setSelectedFlight} = useContext(FlightResultsContext);
    const navigate = useNavigate();

    const handleClick = (flightOffer : any) =>{
        setSelectedFlight(flightOffer);
        console.log(flightOffer);
        navigate('/details');
    };

    const parseDuration = (duration: any) => {
        const match = duration.match(/PT(\d+H)?(\d+M)?/);
        const hours = match[1] ? parseInt(match[1].replace('H', '')) : 0;
        const minutes = match[2] ? parseInt(match[2].replace('M', '')) : 0;
        return `${hours}h ${minutes}m`;
    };

    return (
       
            <Flex vertical className="resultsFlex" justify="center" align="center">
                <List
                    style={{width: '100%'}}
                    dataSource={flightResults.data}
                    renderItem={(offer: any) => (
                        <List.Item onClick={()=>handleClick(offer)} key={offer.id} className="clickable">
                            {offer.itineraries.map((itinerary: any) => (
                                <React.Fragment key={itinerary.id}>
                                    <Flex>
                                        <Col>
                                            {/*<p>Total time: {dayjs(itinerary.duration).format('HH:mm')}</p>*/}
                                            {itinerary.segments.map((segment: any) => (
                                                <Row style={{ marginBottom: '16px' }}>
                                                    <Card key={segment.id}>
                                                        <Row>
                                                            <Paragraph>{dayjs(segment.departure.at).format('YYYY-MM-DD HH:mm')} - {dayjs(segment.arrival.at).format('YYYY-MM-DD HH:mm')}</Paragraph>
                                                        </Row>
                                                        <Row>
                                                            <Col><p>{segment.departure.airportCommonName}({segment.departure.iataCode}) - {segment.arrival.airportCommonName}({segment.arrival.iataCode})</p></Col>
                                                            <Divider type="vertical" style={{ height: '100%' }} />
                                                            <Col><p>{parseDuration(segment.duration)}</p></Col>
                                                        </Row>
                                                    </Card>

                                                </Row>
                                            ))}
                                        </Col>
                                        <Divider type="vertical" style={{ height: '20px' }} />
                                        <Col>
                                            <Row>
                                                <Title level={4}>$ {offer.price.total} {offer.price.currency} Total</Title>
                                            </Row>
                                            <Row>
                                                <ul>
                                                    {offer.travelerPricings.map((price: any) => (
                                                        <li key={price.travelerId}>
                                                            {price.travelerType} {price.travelerId}: $ {price.price.total} {price.price.currency}
                                                        </li>
                                                    ))}
                                                </ul>
                                            </Row>
                                        </Col>
                                    </Flex>
                                </React.Fragment>
                            ))}
                        </List.Item>
                    )}
                />

            </Flex>

    );
};

export default Results;
