import React from "react";
import {createStyles, makeStyles} from "@material-ui/core/styles";
import AppBar from "@material-ui/core/AppBar";
import {Toolbar} from "@material-ui/core";
import Breadcrumbs from "@material-ui/core/Breadcrumbs";
import Link from "@material-ui/core/Link";
import HomeIcon from "@material-ui/icons/Home";
import Typography from "@material-ui/core/Typography";


const useStyles = makeStyles((theme) =>
    createStyles({
        appBar: {
            alignItems: 'center',
            backgroundColor: '#172b4d',
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
    }),
);

export default function ApplicationBar(props) {
    const classes = useStyles();
    const { repository, branch } = props

    return (
        <AppBar className={classes.appBar} position="static">
            <Toolbar>
                    {repository && branch ?
                        (
                            <Breadcrumbs className={classes.typography} aria-label="breadcrumb">
                                <Link color="inherit" href="/" className={classes.flex}>
                                    <HomeIcon className={classes.icon} />
                                    <Typography>
                                        home
                                    </Typography>
                                </Link>
                                <Typography>
                                    {repository}
                                </Typography>
                                <Typography>
                                    {branch}
                                </Typography>
                            </Breadcrumbs>
                        ) :
                        (
                            <Breadcrumbs className={classes.typography} aria-label="breadcrumb">
                                <div className={classes.flex}>
                                    <HomeIcon className={classes.icon} />
                                    <Typography>
                                        git-changes-monitoring
                                    </Typography>
                                </div>
                            </Breadcrumbs>
                        )
                    }
            </Toolbar>
        </AppBar>
    );
}
