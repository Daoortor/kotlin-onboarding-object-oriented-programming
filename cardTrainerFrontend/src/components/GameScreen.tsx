import StartScreen from "./screens/StartScreen";
import GameRoundScreen from "./screens/GameRoundScreen";
import React from "react";
import GameStatisticsScreen from "./screens/GameStatisticsScreen";
import {card} from "common-types";
import JsCardTrainerModel = card.trainer.JsCardTrainerModel;

export enum GameState {
    START,
    GAME,
    GAME_STAT,
}

export type GameScreenProps = {
    state: GameState,
    gameStateSetter: (gs: GameState) => void,
    cardSetter: (card: JsCardTrainerModel) => void,
    currentCard: JsCardTrainerModel,
    wordSetter: (card: string) => void,
    currentWord: string,
    known: string[],
    knownSetter: (known:  string[]) => void,
    unknown: string[],
    unknownSetter: (known:  string[]) => void,
}

export default function GameScreen({
                                       state,
                                       gameStateSetter,
                                       cardSetter,
                                       currentCard,
                                       wordSetter,
                                       currentWord,
                                       known,
                                       knownSetter,
                                       unknown,
                                       unknownSetter
                                   }: GameScreenProps) {
    switch (state) {
        case GameState.START: {
            return <StartScreen gameStateSetter={gameStateSetter}/>
        }
        case GameState.GAME: {
            return <GameRoundScreen
                gameStateSetter={gameStateSetter}
                cardSetter={cardSetter}
                currentCard={currentCard}
                wordSetter={wordSetter}
                currentWord={currentWord}
                known={known}
                knownSetter={knownSetter}
                unknown={unknown}
                unknownSetter={unknownSetter}
            />
        }
        case GameState.GAME_STAT: {
            return <GameStatisticsScreen
                gameStateSetter={gameStateSetter}
                known={known}
                unknown={unknown}
            />
        }
    }
}