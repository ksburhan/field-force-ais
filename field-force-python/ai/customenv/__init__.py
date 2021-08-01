from gym.envs.registration import register

register(
    id='Fieldforce-v0',
    entry_point='customenv.envs:FieldEnv',
    max_episode_steps=2000,
)
