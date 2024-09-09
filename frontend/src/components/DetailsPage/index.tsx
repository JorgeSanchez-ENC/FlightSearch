import React, {useContext} from "react";
import FlightResultsContext from "../../contexts/FlightResultsContext";
import { Card, Col, Divider, Flex, List, Row, Typography } from "antd";
import "./index.css";
import dayjs from "dayjs";

const { Title, Paragraph, Text} = Typography;

const DetailsPage: React.FC = () =>{
    const {selectedFlight} = useContext(FlightResultsContext);
    const traveler = selectedFlight.travelerPricings[0];
    console.log(traveler);
    return(

        <Flex vertical className="detailsFlex" >
            <div>
            <Row align={"middle"}>
                <Col span={24}>
                    <Title>Flight Details</Title>
                
                </Col>
            </Row>

            </div>
            
            <Row>
                <Col span={17}>
                    <List
                        itemLayout="vertical"
                        dataSource={selectedFlight.itineraries}
                        renderItem={(itinerary:any, itineraryIndex : any)=>(
                            
                            <List.Item key={itineraryIndex}>
                                <Title level={3}> {itineraryIndex === 0 ? "Departure" : "Return"}</Title>
                                {itinerary.segments.map((segment:any, segmentIndex: any)=>{
                                    return(
                                        <Row style={{ marginBottom: '16px' }} key={segmentIndex}>
                                            <Card style={{ width: '80%' }}>
                                                <Row >
                                                    <Col span={18}>
                                                        <Title level={4}>Segment</Title> 
                                                        <Row >
                                                            <Col span={12}><Text strong> Dept. date</Text></Col>
                                                            <Col span={12}><Text strong >Arr. date</Text></Col>
                                                        </Row>
                                                        <Row className="marginBottom">
                                                            <Col span={12}><Text>{dayjs(segment.departure.at).format('YYYY-MM-DD HH:mm')} </Text></Col>
                                                            <Col span={12}><Text>{dayjs(segment.arrival.at).format('YYYY-MM-DD HH:mm')}</Text></Col>
                                                        </Row>
                                                        <Row>
                                                            <Col span={12}><Text strong>Dept. airport</Text></Col>
                                                            <Col span={12}><Text strong>Arr. airport</Text></Col>
                                                        </Row>
                                                        <Row className="marginBottom">
                                                            <Col span={12}><Text>{segment.departure.airportCommonName}({segment.departure.iataCode}) </Text></Col>
                                                            <Col span={12}><Text>{segment.arrival.airportCommonName}({segment.arrival.iataCode})</Text></Col>
                                                        </Row>
                                                        <Row>
                                                            <Col span={12}><Text strong>Carrier: </Text><Text>{segment.airlineCommonName} {segment.carrierCode} </Text></Col>
                                                            <Col span={12}><Text strong>Flight number: </Text><Text>{segment.number}</Text></Col>
                                                        </Row>
                                                    </Col>
                                                    {traveler.fareDetailsBySegment.filter((fareDetail :any) => fareDetail.segmentId === segment.id)
                                                    .map((fareDetail:any, fareIndex:any) => (
                                                        <Col span={6} key={fareIndex}>
                                                            <Title level={4}>Travelers fare details</Title>
                                                            <Paragraph>Cabin: {fareDetail.cabin}</Paragraph>
                                                            <Paragraph>Class: {fareDetail.class}</Paragraph>
                                                            <Paragraph>Ammenities:</Paragraph>
                                                            {fareDetail.ammenities ? (
                                                                <ul>
                                                                    {fareDetail.ammenities.map((ammenitie:any, index:any) => (
                                                                        <li key={index}>
                                                                            {ammenitie.description}, {ammenitie.isChargeable ? "chargeable" : "not chargeable"}
                                                                        </li>
                                                                    ))}
                                                                </ul>
                                                            ) : (
                                                                <Paragraph>No ammenities available</Paragraph>
                                                            )}
                                                        </Col>
                                                    ))}
                              
                                                </Row>

                                            </Card>
                                        </Row>

                                    );

                                })}
                            </List.Item>
                        )}
                    >
                    </List>
                </Col>
                <Divider type="vertical" style={{ height: '100%' }} />
                <Col span={6}>
                        <Title level={4}>Price Breakdown</Title>
                        <Paragraph>Base: $ {selectedFlight.price.base} {selectedFlight.price.currency}</Paragraph> 
                        <Paragraph>Fees</Paragraph> 
                        <ul>
                            {selectedFlight.price.fees.map((fee: any)=>(
                                <li> {fee.type}: ${fee.amount} {selectedFlight.price.currency} </li>
                            ))}
                        </ul>
                        <Paragraph>Total: $ {selectedFlight.price.total} {selectedFlight.price.currency}</Paragraph>
                        <Paragraph>Price per traveler</Paragraph>
                        <ul>
                            {selectedFlight.travelerPricings.map((price: any) => (
                                <li key={price.travelerId}>
                                    {price.travelerType} {price.travelerId}: $ {price.price.total} {price.price.currency}
                                </li>
                            ))}
                        </ul>
                </Col>
            </Row>
       </Flex>
        
    );
};

export default DetailsPage;