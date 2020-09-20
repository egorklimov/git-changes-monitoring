import React from "react";
import IconButton from "@material-ui/core/IconButton";
import Dialog from "@material-ui/core/Dialog";
import DialogTitle from "@material-ui/core/DialogTitle";
import DialogContent from "@material-ui/core/DialogContent";
import DialogContentText from "@material-ui/core/DialogContentText";
import TextField from "@material-ui/core/TextField";
import DialogActions from "@material-ui/core/DialogActions";
import Button from "@material-ui/core/Button";
import LinkIcon from "@material-ui/icons/Link";
import Fab from "@material-ui/core/Fab";
import CheckIcon from "@material-ui/icons/Check";
import CircularProgress from "@material-ui/core/CircularProgress";
import makeStyles from "@material-ui/core/styles/makeStyles";
import green from "@material-ui/core/colors/green";

const useStyles = makeStyles((theme) => ({
    root: {
        display: "flex",
        alignItems: "center",
    },
    wrapper: {
        margin: theme.spacing(1),
        position: "relative",
    },
    fabProgress: {
        color: green[500],
        position: "absolute",
        top: -2,
        left: -2,
        zIndex: 1,
    },
    buttonProgress: {
        color: green[500],
        position: "absolute",
        top: "50%",
        left: "50%",
        marginTop: 3,
        marginLeft: 3,
    },
}));

export default function AddRepositoryDialog(props) {
    const classes = useStyles();
    const { handleCloneError } = props;
    const [open, setOpen] = React.useState(false);
    const [repository, setRepository] = React.useState();
    const [isCloning, setIsCloning] = React.useState(false);

    const handleClickOpen = () => setOpen(true);
    const handleClose = () => setOpen(false);

    const handleSubmit = async () => {
        setIsCloning(true);
        const response = await fetch("/repository/clone", {
            method: "POST",
            headers: {
                "Content-Type": "application/json;charset=utf-8",
            },
            body: JSON.stringify({
                url: repository
            })
        }).then((response) => {
           if (!response.ok) {
               handleCloneError(response);
           }
           return response;
        });
        await Promise.all([
            response.json(),
            setIsCloning(false),
            setOpen(false),
        ]);
    };

    const handleChange = (e) => {
        e.preventDefault();
        setRepository(e.target.value);
    };

    return (
        <div>
            <IconButton onClick={handleClickOpen}>
                <LinkIcon />
            </IconButton>
            <Dialog open={open} onClose={handleClose} aria-labelledby="form-dialog-title">
                <DialogTitle id="form-dialog-title">Github repository</DialogTitle>
                <DialogContent>
                    <DialogContentText>
                        To add a public repository from Github, please, enter the link to the repository.
                        Name is case sensitive, and must end with .git.
                        <br/>
                        It may take a while to get the repository, please do not cancel the download.
                    </DialogContentText>
                    <TextField
                        autoComplete="off"
                        autoFocus
                        margin="dense"
                        id="name"
                        placeholder="https://github.com/egorklimov/git-changes-monitoring.git"
                        label="Repository link"
                        fullWidth
                        onChange={handleChange}
                    />
                </DialogContent>
                <DialogActions>
                    {isCloning ?
                        (
                            <div className={classes.wrapper}>
                                <Fab
                                    aria-label="save"
                                    color="primary"
                                >
                                    <CheckIcon />
                                </Fab>
                                <CircularProgress size={60} className={classes.fabProgress} />
                            </div>
                    ) :
                        (
                            <div>
                                <Button onClick={handleClose} color="primary">
                                    Cancel
                                </Button>
                                <Button onClick={handleSubmit} color="primary">
                                    Clone
                                </Button>
                            </div>
                        )
                    }
                </DialogActions>
            </Dialog>
        </div>
    );
}