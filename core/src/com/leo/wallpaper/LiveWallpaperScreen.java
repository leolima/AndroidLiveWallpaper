package com.leo.wallpaper;

import static com.badlogic.gdx.Gdx.gl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

public class LiveWallpaperScreen implements Screen{

    MyGdxGame game;
    Stage stage;
    Viewport vp;
    OrthographicCamera camera;
    Texture textureBg;
    SpriteBatch batcher;
    Skin skin;
    TextButton textButton;
    Label alert;
    FileHandle effect;
    TextureAtlas particlesAtlas;
    ArrayList<ParticleEffect> particleEffects;

    public LiveWallpaperScreen(final MyGdxGame game) {
        this.game = game;

        batcher = new SpriteBatch();
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        vp = new FitViewport(camera.viewportWidth, camera.viewportHeight, new OrthographicCamera());
        stage = new Stage(vp, batcher);

        skin = new Skin(Gdx.files.internal("skins/uiskin.json"));

        textureBg = new Texture("badlogic.jpg");
        textureBg.setFilter(TextureFilter.Linear, TextureFilter.Linear);

        Table table = new Table();
        table.bottom();
        table.setFillParent(true);

        alert = new Label("Your device does not support wallpapers", skin);
        alert.setPosition(((Gdx.graphics.getWidth() /2)-alert.getWidth()), 20);

        textButton = new TextButton("Set as wallpaper", skin, "default");
        textButton.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (Gdx.app.getType().toString().equals("Android")) {
                    game.wallpaperHandling.askSetWallpaper();
                } else {
                    if(!stage.getActors().contains(alert, true)){
                        stage.addActor(alert);
                    }
                    SequenceAction sequence = new SequenceAction();
                    sequence.addAction(Actions.fadeIn(0.3f));
                    sequence.addAction(Actions.fadeOut(3f));
                    sequence.addAction(Actions.removeActor());
                    alert.addAction(sequence);
                }

                return super.touchDown(event, x, y, pointer, button);
            }
        });

        table.row();
        table.add(textButton).center().pad(5).padBottom(50);
        stage.addActor(table);

        effect = Gdx.files.internal("effects/fire.p");
        particlesAtlas = new TextureAtlas(Gdx.files.internal("atlas/particles.atlas"));
        particleEffects = new ArrayList<>();

        InputMultiplexer im = new InputMultiplexer();
        im.addProcessor(new InputProcessor() {
            @Override
            public boolean keyDown(int keycode) {
                return false;
            }

            @Override
            public boolean keyUp(int keycode) {
                return false;
            }

            @Override
            public boolean keyTyped(char character) {
                return false;
            }

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                ParticleEffect newParticleEffect = new ParticleEffect();
                newParticleEffect.load(effect,particlesAtlas);
                particleEffects.add(newParticleEffect);
                newParticleEffect.setPosition(screenX, Gdx.graphics.getHeight() - screenY);
                newParticleEffect.start();
                return false;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                return false;
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                return false;
            }

            @Override
            public boolean mouseMoved(int screenX, int screenY) {
                return false;
            }

            @Override
            public boolean scrolled(float amountX, float amountY) {
                return false;
            }
        });
        im.addProcessor(stage);

        Gdx.input.setInputProcessor(im);
    }

    @Override
    public void dispose() {
        batcher.dispose();
        particlesAtlas.dispose();
        textureBg.dispose();
        skin.dispose();
        stage.dispose();
        for(int i = 0; i< particleEffects.size(); i++) {
            particleEffects.get(i).dispose();
        }
    }

    @Override
    public void hide() {
        // TODO Auto-generated method stub
    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub
    }

    private void draw(float delta) {
        gl.glClearColor(0, 0, 0, 1);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        update(delta);
        batcher.setProjectionMatrix(camera.combined);
        batcher.begin();
        for(ParticleEffect particle: particleEffects) {
            particle.draw(batcher);
        }
        batcher.end();
        stage.draw();
    }

    private void update(float delta) {
        camera.update();
        stage.act(delta);
        for(ParticleEffect particle: particleEffects) {
            particle.update(delta);
        }
        for(int i = 0; i< particleEffects.size(); i++) {
            if(particleEffects.get(i).isComplete()){
                particleEffects.remove(i);
            }
        }
    }

    @Override
    public void render(float delta) {
        update(delta);
        draw(delta);
    }

    @Override
    public void resize(int width, int height) {
        // TODO Auto-generated method stub
    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub
    }

    @Override
    public void show() {
        // TODO Auto-generated method stub
    }
}
