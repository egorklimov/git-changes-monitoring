import React, {useEffect} from "react";
import {createStyles, makeStyles} from "@material-ui/core/styles";
import {Container} from "@material-ui/core";
import Tree from "./Tree";
import SpeedDial from "@material-ui/lab/SpeedDial";
import SpeedDialIcon from "@material-ui/lab/SpeedDialIcon";
import StarBorderIcon from '@material-ui/icons/StarBorder';
import SpeedDialAction from "@material-ui/lab/SpeedDialAction";
import Link from "@material-ui/core/Link";
import AddOrganizationDialog from "./action/organization/AddOrganizationDialog";
import AddRepositoryDialog from "./action/AddRepositoryDialog";
import ApplicationBar from "../common/ApplicationBar";
import Loading from "../common/Loading";

const useStyles = makeStyles((theme) =>
    createStyles({
        appBar: {
            backgroundColor: '#172b4d',
            alignItems: 'center',
        },
        typography: {
            color: "#e5ecf5",
        },
        icon: {
            marginRight: theme.spacing(0.5),
            width: 20,
            height: 20,
        },
        flex: {
            display: 'flex',
        },
        exampleWrapper: {
            position: 'relative',
            marginTop: theme.spacing(3),
            height: 380,
        },
        speedDial: {
            position: 'absolute',
            '&.MuiSpeedDial-directionUp, &.MuiSpeedDial-directionLeft': {
                bottom: theme.spacing(2),
                right: theme.spacing(2),
            },
            '&.MuiSpeedDial-directionDown, &.MuiSpeedDial-directionRight': {
                top: theme.spacing(2),
                left: theme.spacing(2),
            },
        },
    }),
);

const actions = [
    {
        icon: <AddOrganizationDialog />,
        name: 'Scan github organization',
    },
    {
        icon: <AddRepositoryDialog />,
        name: 'Add github repository by link'
    },
    {
        icon: (
            <Link
                href="https://github.com/egorklimov/git-changes-monitoring"
                target="_blank"
            >
                <StarBorderIcon />
            </Link>
        ),
        name: 'Star egorklimov/git-changes-monitoring on GitHub',
    },
];

export default function Home() {
    const classes = useStyles();
    const [open, setOpen] = React.useState(false);
    const [isLoaded, setIsLoaded] = React.useState(false);
    const [organizations, setOrganizations] = React.useState();
    const [error, setError] = React.useState();

    const loadOrganizations = () => {
        fetch(
            `/tag`, {
                method: "GET",
                headers: {
                    "Content-Type": "application/json;charset=utf-8",
                },
            }).then((response) => {
            if (!response.ok) {
                throw {message: response.status};
            }
            return response.json();
        }).then((result) => {
            setOrganizations(result);
            setIsLoaded(true);
            },
            (error) => {
                setIsLoaded(true);
                setError(`Received ${error.message} from backend.`);
            }
        );
    };

    useEffect(() => {
        loadOrganizations();
    }, []);

    const handleClose = () => {
        setOpen(false);
    };

    const handleOpen = () => {
        setOpen(true);
    };

    // if (error) {
    //     return (
    //         <div>
    //             <ApplicationBar/>
    //             <Error error={error}/>
    //         </div>
    //     );
    // }
    if (!isLoaded) {
        return <Loading/>;
    }
    return (
        <div>
            <ApplicationBar/>
            <Container maxWidth="md">
                <Tree
                    organizations={[
                        {name: 'JetBrains-Research', repositories: [{name: 'snakecharm', branches: [{shortName: 'master'}]}]},
                        {name: 'Research', repositories: [{name: 'notsnakecharm', branches: [{shortName: 'master'}]}]},
                    ]}
                />

                <div className={classes.exampleWrapper}>
                    <SpeedDial
                        ariaLabel="SpeedDial example"
                        className={classes.speedDial}
                        icon={<SpeedDialIcon />}
                        onClose={handleClose}
                        onOpen={handleOpen}
                        open={open}
                        direction={"left"}
                    >
                        {actions.map((action) => (
                            <SpeedDialAction
                                title={action.name}
                                key={action.name}
                                icon={action.icon}
                                tooltipTitle={action.name}
                                onClick={handleClose}
                            />
                        ))}
                    </SpeedDial>
                </div>
            </Container>
        </div>
    );
}